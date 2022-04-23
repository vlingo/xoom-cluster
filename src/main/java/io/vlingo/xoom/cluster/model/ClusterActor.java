package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.List;
import java.util.stream.Collectors;

public class ClusterActor extends Actor implements ClusterControl, InboundStreamInterest {
  private final ClusterApplication clusterApplication; // only one application for now
  private final ClusterImpl cluster;
  private final ClusterCommunicationsHub communicationsHub;

  public ClusterActor(final ClusterInitializer initializer, final ClusterApplication clusterApplication) throws Exception {
    this.clusterApplication = clusterApplication;

    Node localNode = initializer.localNode();
    String localNodeHostName = localNode.operationalAddress().hostName();
    int localNodePort = localNode.operationalAddress().port();
    List<Address> seeds = initializer.configuration().allSeeds().stream()
        .map(address -> Address.create(address.hostName(), address.port()))
        .collect(Collectors.toList());

    ClusterConfig config = new ClusterConfig()
        .memberAlias(localNode.name().value())
        .externalHost(localNodeHostName)
        .externalPort(localNodePort)
        .membership(membershipConfig -> membershipConfig.seedMembers(seeds))
        .transport(transportConfig -> transportConfig.port(localNodePort).transportFactory(new TcpTransportFactory()));

    this.cluster = new ClusterImpl(config)
        .handler(c -> new MessageHandler(logger(), clusterApplication, c, initializer));

    this.communicationsHub = initializer.communicationsHub();
    this.communicationsHub.open(stage(), initializer.localNode(), selfAs(InboundStreamInterest.class), initializer.configuration());

    this.clusterApplication.start();
    this.clusterApplication.informResponder(communicationsHub.applicationOutboundStream());

    this.cluster.startAwait();
    initializer.registry().join(localNode);
  }

  @Override
  public void handleInboundStreamMessage(AddressType addressType, RawMessage message) {
    if (isStopped()) {
      return;
    }

    if (addressType.isApplication()) {
      clusterApplication.handleApplicationMessage(message);
    } else {
      logger().warn("ClusterActor couldn't dispatch incoming message; unknown address type: " +
              addressType + " for message: " + message.asTextMessage());
    }
  }

  @Override
  public void shutDown() {
    if (isStopped()) {
      return;
    }

    cluster.shutdown();
    clusterApplication.stop();
    communicationsHub.close();

    stop();
    stage().world().terminate();
  }

  private static final class MessageHandler implements ClusterMessageHandler {
    private final Logger logger;
    private final ClusterApplication clusterApplication;
    private final io.scalecube.cluster.Cluster cluster;
    private final Registry registry;
    private final ClusterConfiguration configuration;
    private final Properties properties;

    private MessageHandler(Logger logger, ClusterApplication clusterApplication, io.scalecube.cluster.Cluster cluster,
                           ClusterInitializer initializer) {
      this.logger = logger;
      this.clusterApplication = clusterApplication;
      this.cluster = cluster;
      this.registry = initializer.registry();
      this.configuration = initializer.configuration();
      this.properties = initializer.properties();
    }

    @Override
    public void onMembershipEvent(MembershipEvent event) {
      logger.debug("Received cluster membership event: " + event);
      String nodeName = event.member().alias();

      if (nodeName != null && nodeName.startsWith("seed")) {
        if (event.isAdded()) {
          clusterApplication.informClusterIsHealthy(true);
        } else if (event.isRemoved() || event.isLeaving()) {
          clusterApplication.informClusterIsHealthy(false);
        }
      } else {
        Node sender = configuration.nodeMatching(Id.of(properties.nodeId(nodeName)));

        if (event.isAdded()) {
          registry.join(sender);
          clusterApplication.informNodeJoinedCluster(sender.id(), true);
          clusterApplication.informNodeIsHealthy(sender.id(), true);
          informAllLiveNodes();
        } else if (event.isLeaving() || event.isRemoved()) {
          registry.leave(sender.id());
          clusterApplication.informNodeLeftCluster(sender.id(), true);
          informAllLiveNodes();
        } else {
          logger.warn("Event type: " + event.type() + " is not handled for now. Received from cluster member: " + event.member());
        }
      }
    }

    private void informAllLiveNodes() {
      List<Node> liveNodes = cluster.members().stream()
          .filter(member -> member.alias() != null && !member.alias().startsWith("seed"))
          .map(member -> configuration.nodeMatching(Id.of(properties.nodeId(member.alias()))))
          .collect(Collectors.toList());

      clusterApplication.informAllLiveNodes(liveNodes, true);
    }
  }
}

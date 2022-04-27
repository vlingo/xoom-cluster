// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.common.Scheduled;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Node;

import java.util.List;
import java.util.stream.Collectors;

public class ClusterActor extends Actor implements ClusterControl, InboundStreamInterest, Scheduled<Object> {
  private final Registry registry;
  private final ClusterApplication clusterApplication; // only one application for now
  private final ClusterImpl cluster;
  private final ClusterMembershipControl membershipControl;
  private final ClusterCommunicationsHub communicationsHub;

  public ClusterActor(final ClusterInitializer initializer, final ClusterApplication clusterApplication) throws Exception {
    this.registry = initializer.registry();
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

    this.membershipControl = new ClusterMembershipControl(logger(), clusterApplication, initializer);
    this.cluster = new ClusterImpl(config)
        .handler(c -> {
          this.membershipControl.setCluster(c);
          return new ClusterMessagingHandler(logger(), membershipControl, initializer.configuration(), initializer.properties());
        });

    this.communicationsHub = initializer.communicationsHub();
    this.communicationsHub.open(stage(), initializer.localNode(), selfAs(InboundStreamInterest.class), initializer.configuration());

    this.clusterApplication.start();
    this.clusterApplication.informResponder(communicationsHub.applicationOutboundStream());

    this.cluster.startAwait();
    initializer.registry().join(localNode);

    stage().scheduler()
        .scheduleOnce(selfAs(Scheduled.class), null, 150L, initializer.properties().clusterStartupPeriod());
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
  public void intervalSignal(Scheduled<Object> scheduled, Object data) {
    registry.startupIsCompleted();
  }

  @Override
  public void shutDown() {
    if (isStopped()) {
      return;
    }

    clusterApplication.stop();
    communicationsHub.close();
    cluster.shutdown();

    stop();
    stage().world().terminate();
  }
}

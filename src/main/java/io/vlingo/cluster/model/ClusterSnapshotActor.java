// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.Collection;

import io.vlingo.actors.Actor;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.attribute.AttributesAgent;
import io.vlingo.cluster.model.message.OperationalMessage;
import io.vlingo.cluster.model.node.LocalLiveNode;
import io.vlingo.cluster.model.node.MergeResult;
import io.vlingo.cluster.model.node.RegistryInterest;
import io.vlingo.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Node;

public class ClusterSnapshotActor
  extends Actor
  implements ClusterSnapshot, ClusterSnapshotControl, InboundStreamInterest, RegistryInterest {

  private final AttributesAgent attributesAgent;
  private final ClusterApplication clusterApplication;
  private final ClusterApplicationBroadcaster broadcaster;
  private final CommunicationsHub communicationsHub;
  private final LocalLiveNode localLiveNode;
  private final Node localNode;

  public ClusterSnapshotActor(final ClusterSnapshotInitializer initializer, final ClusterApplication clusterApplication) throws Exception {
    this.broadcaster = new ClusterApplicationBroadcaster(logger());
    this.communicationsHub = initializer.communicationsHub();
    this.communicationsHub.open(stage(), initializer.localNode(), selfAs(InboundStreamInterest.class), initializer.configuration());
    this.localNode = initializer.localNode();
    this.clusterApplication = clusterApplication;
    this.broadcaster.registerClusterApplication(clusterApplication);
    clusterApplication.start();
    clusterApplication.informResponder(communicationsHub.applicationOutboundStream());

    initializer.registry().registerRegistryInterest(selfAs(RegistryInterest.class));

    this.attributesAgent =
            AttributesAgent.instance(
                    stage(),
                    this.localNode,
                    this.broadcaster,
                    communicationsHub.operationalOutboundStream(),
                    initializer.configuration());

    this.localLiveNode =
            LocalLiveNode.instance(
                    stage(),
                    this.localNode,
                    selfAs(ClusterSnapshot.class),
                    initializer.registry(),
                    communicationsHub.operationalOutboundStream(),
                    initializer.configuration());

    this.localLiveNode.registerNodeSynchronizer(this.attributesAgent);

    this.communicationsHub.start();
  }

  //=========================================
  // ClusterSnapshot
  //=========================================

  @Override
  public void quorumAchieved() {
    broadcaster.informQuorumAchieved();
  }

  @Override
  public void quorumLost() {
    broadcaster.informQuorumLost();
  }


  //=========================================
  // ClusterSnapshotControl
  //=========================================

  @Override
  public void shutDown() {
    if (isStopped()) {
      Cluster.reset();
      return;
    }

    localLiveNode.stop();
    clusterApplication.stop();
    attributesAgent.stop();
    communicationsHub.close();
    stop();
    stage().world().terminate();
    Cluster.reset();
  }


  //=========================================
  // InboundStreamInterest (operations and application)
  //=========================================

  @Override
  public void handleInboundStreamMessage(final AddressType addressType, final RawMessage message) {
    if (isStopped()) {
      return;
    }

    if (addressType.isOperational()) {
      final String textMessage = message.asTextMessage();
      final OperationalMessage typedMessage = OperationalMessage.messageFrom(textMessage);
      if (typedMessage != null) {
        if (typedMessage.isApp()) {
          attributesAgent.handleInboundStreamMessage(addressType, message);
        } else {
          localLiveNode.handle(typedMessage);
        }
      } else {
        logger().warn("ClusterSnapshot received invalid raw message '{}'", textMessage);
      }
    } else if (addressType.isApplication()) {
      clusterApplication.handleApplicationMessage(message); // TODO
    } else {
      logger().warn(
              "ClusterSnapshot couldn't dispatch incoming message; unknown address type: " +
              addressType +
              " for message: " +
              message.asTextMessage());
    }
  }


  //=========================================
  // RegistryInterest
  //=========================================

  @Override
  public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
    broadcaster.informAllLiveNodes(liveNodes, isHealthyCluster);
  }

  @Override
  public void informConfirmedByLeader(final Node node, final boolean isHealthyCluster) {
    broadcaster.informNodeIsHealthy(node.id(), isHealthyCluster);
  }

  @Override
  public void informCurrentLeader(final Node node, final boolean isHealthyCluster) {
    broadcaster.informLeaderElected(node.id(), isHealthyCluster, node.id().equals(localNode.id()));
  }

  @Override
  public void informMergedAllDirectoryEntries(
          final Collection<Node> liveNodes,
          final Collection<MergeResult> mergeResults,
          final boolean isHealthyCluster) {

    for (final MergeResult mergeResult : mergeResults) {
      if (mergeResult.left()) {
        broadcaster.informNodeLeftCluster(mergeResult.node().id(), isHealthyCluster);
      } else if (mergeResult.joined()) {
        broadcaster.informNodeJoinedCluster(mergeResult.node().id(), isHealthyCluster);
      }
    }
  }

  @Override
  public void informLeaderDemoted(final Node node, final boolean isHealthyCluster) {
    broadcaster.informLeaderLost(node.id(), isHealthyCluster);
  }

  @Override
  public void informNodeIsHealthy(final Node node, final boolean isHealthyCluster) {
    broadcaster.informNodeIsHealthy(node.id(), isHealthyCluster);
  }

  @Override
  public void informNodeJoinedCluster(final Node node, final boolean isHealthyCluster) {
    broadcaster.informNodeJoinedCluster(node.id(), isHealthyCluster);
  }

  @Override
  public void informNodeLeftCluster(final Node node, final boolean isHealthyCluster) {
    broadcaster.informNodeLeftCluster(node.id(), isHealthyCluster);
  }

  @Override
  public void informNodeTimedOut(final Node node, final boolean isHealthyCluster) {
    broadcaster.informNodeLeftCluster(node.id(), isHealthyCluster);
  }
}

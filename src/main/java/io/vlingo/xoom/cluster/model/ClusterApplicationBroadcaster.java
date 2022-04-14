// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.attribute.AttributesProtocol;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

class ClusterApplicationBroadcaster implements ClusterApplication {
  private List<ClusterApplication> clusterApplications;
  private final Logger logger;

  ClusterApplicationBroadcaster(final Logger logger) {
    this.logger = logger;
    this.clusterApplications = new ArrayList<>();
  }

  public void registerClusterApplication(final ClusterApplication clusterApplication) {
    clusterApplications.add(clusterApplication);
  }

  //========================================
  // ClusterApplication
  //========================================

  @Override
  public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
    broadcast((app) -> app.informAllLiveNodes(liveNodes, isHealthyCluster));
  }

  @Override
  public void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading) {
    broadcast((app) -> app.informLeaderElected(leaderId, isHealthyCluster, isLocalNodeLeading));
  }

  @Override
  public void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster) {
    broadcast((app) -> app.informLeaderLost(lostLeaderId, isHealthyCluster));
  }

  @Override
  public void informLocalNodeShutDown(final Id nodeId) {
    broadcast((app) -> app.informLocalNodeShutDown(nodeId));
  }

  @Override
  public void informLocalNodeStarted(final Id nodeId) {
    broadcast((app) -> app.informLocalNodeStarted(nodeId));
  }

  @Override
  public void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster) {
    broadcast((app) -> app.informNodeIsHealthy(nodeId, isHealthyCluster));
  }

  @Override
  public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
    broadcast((app) -> app.informNodeJoinedCluster(nodeId, isHealthyCluster));
  }

  @Override
  public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
    broadcast((app) -> app.informNodeLeftCluster(nodeId, isHealthyCluster));
  }

  @Override
  public void informQuorumAchieved() {
    broadcast(ClusterApplication::informQuorumAchieved);
  }

  @Override
  public void informQuorumLost() {
    broadcast(ClusterApplication::informQuorumLost);
  }

  @Override
  public void informResponder(final ApplicationOutboundStream responder) {
    broadcast((app) -> app.informResponder(responder));
  }

  @Override
  public void informAttributesClient(final AttributesProtocol client) {
    broadcast((app) -> app.informAttributesClient(client));
  }

  @Override
  public void informAttributeSetCreated(final String attributeSetName) {
    broadcast((app) -> app.informAttributeSetCreated(attributeSetName));
  }

  @Override
  public void informAttributeAdded(final String attributeSetName, final String attributeName) {
    broadcast((app) -> app.informAttributeAdded(attributeSetName, attributeName));
  }

  @Override
  public void informAttributeRemoved(final String attributeSetName, final String attributeName) {
    broadcast((app) -> app.informAttributeRemoved(attributeSetName, attributeName));
  }

  @Override
  public void informAttributeSetRemoved(final String attributeSetName) {
    broadcast((app) -> app.informAttributeSetRemoved(attributeSetName));
  }

  @Override
  public void informAttributeReplaced(final String attributeSetName, final String attributeName) {
    broadcast((app) -> app.informAttributeReplaced(attributeSetName, attributeName));
  }

  @Override
  public void start() {
  }

  @Override
  public void conclude() {
  }

  @Override
  public boolean isStopped() {
    return false;
  }

  @Override
  public void stop() {
  }

  @Override
  public void handleApplicationMessage(final RawMessage message) {
    broadcast((app) -> app.handleApplicationMessage(message));
  }

  private void broadcast(final Consumer<ClusterApplication> inform) {
    for (final ClusterApplication app : clusterApplications) {
      try {
        inform.accept(app);
      } catch (Exception e) {
        logger.error("Cannot inform because: " + e.getMessage(), e);
      }
    }
  }
}

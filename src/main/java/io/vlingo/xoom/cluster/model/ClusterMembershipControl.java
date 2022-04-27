// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.Cluster;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class ClusterMembershipControl {
  private final Logger logger;
  private Cluster cluster;
  private final ClusterApplication clusterApplication;
  private final Registry registry;

  private final ClusterConfiguration configuration;
  private final Properties properties;

  private final AtomicBoolean startupCompleted = new AtomicBoolean(false); // startup grace period
  private final AtomicInteger liveNodeCount = new AtomicInteger(0);

  // Number of seeds is in config file. Does it influence isHealthyCluster?
  private final AtomicInteger liveSeedCount = new AtomicInteger(0);

  ClusterMembershipControl(Logger logger, ClusterApplication clusterApplication, ClusterInitializer initializer) {
    this.logger = logger;
    this.clusterApplication = clusterApplication;
    this.registry = initializer.registry();
    this.configuration = initializer.configuration();
    this.properties = initializer.properties();
  }

  public void seedAdded(String seedName) {
    liveSeedCount.incrementAndGet();
  }

  public void seedRemoved(String seedName) {
    liveSeedCount.decrementAndGet();
  }

  public void seedLeaving(String seedName) {
    liveSeedCount.decrementAndGet();
  }

  public void nodeAdded(Node node) {
    int currentNodesCount = liveNodeCount.incrementAndGet();
    boolean isHealthyCluster = isHealthyCluster(currentNodesCount);

    registry.join(node);
    clusterApplication.informNodeJoinedCluster(node.id(), isHealthyCluster);
    clusterApplication.informNodeIsHealthy(node.id(), isHealthyCluster);
    informAllLiveNodes();
  }

  public void nodeRemoved(Node node) {
    int currentNodesCount = liveNodeCount.decrementAndGet();
    boolean isHealthyCluster = isHealthyCluster(currentNodesCount);

    registry.leave(node.id());
    clusterApplication.informNodeLeftCluster(node.id(), isHealthyCluster);
    informAllLiveNodes();
  }

  public void nodeLeaving(Node node) {
    nodeRemoved(node);
  }

  public void startupIsCompleted() {
    startupCompleted.set(true);
  }

  public void setCluster(Cluster cluster) {
    this.cluster = cluster;
  }

  private boolean isHealthyCluster(int currentNodesCount) {
    return currentNodesCount >= properties.clusterMinimumNodes() && startupCompleted.get();
  }

  private void informAllLiveNodes() {
    List<Node> liveNodes = cluster.members().stream()
        .filter(member -> member.alias() != null && !member.alias().startsWith("seed"))
        .map(member -> configuration.nodeMatching(Id.of(properties.nodeId(member.alias()))))
        .collect(Collectors.toList());

    clusterApplication.informAllLiveNodes(liveNodes, true);
  }
}

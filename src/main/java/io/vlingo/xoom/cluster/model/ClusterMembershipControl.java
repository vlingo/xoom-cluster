// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Node;

import java.util.concurrent.atomic.AtomicInteger;

class ClusterMembershipControl {
  private final ClusterApplication clusterApplication;
  private final ClusterMembershipInterest interest; // single instance for now; List?
  private final Registry registry;

  // Number of seeds is in config file. Does it influence isHealthyCluster? Move it to Registry?
  private final AtomicInteger liveSeedCount = new AtomicInteger(0);

  ClusterMembershipControl(ClusterApplication clusterApplication, ClusterMembershipInterest interest, ClusterInitializer initializer) {
    this.clusterApplication = clusterApplication;
    this.interest = interest;
    this.registry = initializer.registry();
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
    boolean before = registry.isClusterHealthy();
    registry.join(node);

    boolean after = registry.isClusterHealthy();
    interest.nodeAdded(node, after);
    clusterApplication.informNodeJoinedCluster(node.id(), after);
    informAllLiveNodes(after);

    if (before != after) {
      clusterApplication.informClusterIsHealthy(after);
      interest.informClusterIsHealthy(after);
    }
  }

  public void nodeRemoved(Node node) {
    boolean before = registry.isClusterHealthy();
    registry.leave(node.id());

    boolean after = registry.isClusterHealthy();
    interest.nodeLeft(node, after);
    clusterApplication.informNodeLeftCluster(node.id(), after);
    informAllLiveNodes(after);

    if (before != after) {
      clusterApplication.informClusterIsHealthy(after);
      interest.informClusterIsHealthy(after);
    }
  }

  public void nodeLeaving(Node node) {
    nodeRemoved(node);
  }

  private void informAllLiveNodes(boolean isClusterHealthy) {
    clusterApplication.informAllLiveNodes(registry.nodes(), isClusterHealthy);
  }
}

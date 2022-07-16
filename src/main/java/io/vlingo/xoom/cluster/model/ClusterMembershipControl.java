// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

class ClusterMembershipControl {
  private final ClusterApplication clusterApplication;
  private final ClusterMembershipInterest interest; // single instance for now; List?
  private final Registry registry;

  ClusterMembershipControl(ClusterApplication clusterApplication, ClusterMembershipInterest interest, ClusterInitializer initializer) {
    this.clusterApplication = clusterApplication;
    this.interest = interest;
    this.registry = initializer.registry();
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

  public void nodeRemoved(Id nodeId) {
    Node node = registry.getNode(nodeId);
    boolean before = registry.isClusterHealthy();
    registry.leave(nodeId);

    boolean after = registry.isClusterHealthy();
    interest.nodeLeft(node, after);
    clusterApplication.informNodeLeftCluster(node.id(), after);
    informAllLiveNodes(after);

    if (before != after) {
      clusterApplication.informClusterIsHealthy(after);
      interest.informClusterIsHealthy(after);
    }
  }

  public void nodeLeaving(Id nodeId) {
    nodeRemoved(nodeId);
  }

  private void informAllLiveNodes(boolean isClusterHealthy) {
    clusterApplication.informAllLiveNodes(registry.nodes(), isClusterHealthy);
  }
}

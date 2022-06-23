// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Node;

import java.util.concurrent.atomic.AtomicInteger;

class ClusterMembershipControl {
  private final Logger logger;
  private final ClusterApplication clusterApplication;
  private final Registry registry;

  private final Properties properties;
  private final ClusterConfiguration configuration;

  // Number of seeds is in config file. Does it influence isHealthyCluster? Move it to Registry?
  private final AtomicInteger liveSeedCount = new AtomicInteger(0);

  ClusterMembershipControl(Logger logger, ClusterApplication clusterApplication, ClusterInitializer initializer) {
    this.logger = logger;
    this.clusterApplication = clusterApplication;
    this.registry = initializer.registry();
    this.properties = initializer.properties();
    this.configuration = initializer.configuration();
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

    clusterApplication.informNodeJoinedCluster(node.id(), after);
    informAllLiveNodes(after);
    if (before != after) {
      clusterApplication.informClusterIsHealthy(after);
    }
  }

  public void nodeRemoved(Node node) {
    boolean before = registry.isClusterHealthy();
    registry.leave(node.id());
    boolean after = registry.isClusterHealthy();

    clusterApplication.informNodeLeftCluster(node.id(), after);
    informAllLiveNodes(after);
    if (before != after) {
      clusterApplication.informClusterIsHealthy(after);
    }
  }

  public void nodeLeaving(Node node) {
    nodeRemoved(node);
  }

  private void informAllLiveNodes(boolean isClusterHealthy) {
    clusterApplication.informAllLiveNodes(registry.nodes(), isClusterHealthy);
  }
}

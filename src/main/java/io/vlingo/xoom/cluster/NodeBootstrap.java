// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.cluster.model.Cluster;
import io.vlingo.xoom.cluster.model.ClusterSnapshotControl;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.cluster.model.application.ClusterApplication.ClusterApplicationInstantiator;
import io.vlingo.xoom.cluster.model.application.ClusterApplication.DefaultClusterApplicationInstantiator;
import io.vlingo.xoom.common.Tuple2;

public final class NodeBootstrap {
  private final Tuple2<ClusterSnapshotControl, Logger> clusterSnapshotControl;

  public static void main(final String[] args) throws Exception {
    if (args.length == 1) {
      boot(args[0]);
    } else if (args.length > 1) {
      System.err.println("XOOM: Too many arguments; provide node name only.");
      System.exit(1);
    } else {
      System.err.println("XOOM: This node must be named with a command-line argument.");
      System.exit(1);
    }
  }

  public static NodeBootstrap boot(final String nodeName) throws Exception {
    return boot(nodeName, false);
  }

  public static NodeBootstrap boot(final String nodeName, final boolean embedded) throws Exception {
    return boot(World.start("xoom-cluster"), nodeName, embedded);
  }

  public static NodeBootstrap boot(final World world, final String nodeName, final boolean embedded) throws Exception {
    return boot(World.start("xoom-cluster"), new DefaultClusterApplicationInstantiator(), Properties.instance(), nodeName, embedded);
  }

  public static NodeBootstrap boot(final World world, final ClusterApplicationInstantiator<?> instantiator, final Properties properties, final String nodeName, final boolean embedded) throws Exception {
    NodeBootstrap instance;

    Properties.instance().validateRequired(nodeName);

    final Tuple2<ClusterSnapshotControl, Logger> control = Cluster.controlFor(world, instantiator, properties, nodeName);

    instance = new NodeBootstrap(control, nodeName);

    control._2.info("Successfully started cluster node: '" + nodeName + "'");

    if (!embedded) {
      control._2.info("==========");
    }

    return instance;
  }

  public ClusterSnapshotControl clusterSnapshotControl() {
    return clusterSnapshotControl._1;
  }

  private NodeBootstrap(final Tuple2<ClusterSnapshotControl, Logger> control, final String nodeName) throws Exception {
    this.clusterSnapshotControl = control;

    ShutdownHook shutdownHook = new ShutdownHook(nodeName, control);
    shutdownHook.register();
  }
}

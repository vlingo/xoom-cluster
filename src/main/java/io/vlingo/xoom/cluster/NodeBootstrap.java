// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.cluster.model.Cluster;
import io.vlingo.xoom.cluster.model.ClusterControl;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.cluster.model.application.ClusterApplication.ClusterApplicationInstantiator;
import io.vlingo.xoom.cluster.model.application.ClusterApplication.DefaultClusterApplicationInstantiator;
import io.vlingo.xoom.common.Tuple2;

public final class NodeBootstrap {
  private final Tuple2<ClusterControl, Logger> clusterControl;

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

  public static NodeBootstrap boot(final String localNodeProperties) throws Exception {
    return boot(localNodeProperties, false);
  }

  public static NodeBootstrap boot(final String localNodeProperties, final boolean embedded) throws Exception {
    return boot(World.start("xoom-cluster"), localNodeProperties, embedded);
  }

  public static NodeBootstrap boot(final World world, final String localNodeProperties, final boolean embedded) throws Exception {
    return boot(World.start("xoom-cluster"), new DefaultClusterApplicationInstantiator(), Properties.instance(), localNodeProperties, embedded);
  }

  public static NodeBootstrap boot(final World world, final ClusterApplicationInstantiator<?> instantiator, final Properties properties, final String localNodeProperties, final boolean embedded) throws Exception {
    NodeBootstrap instance;

    Properties.instance().validateRequired();

    final Tuple2<ClusterControl, Logger> control = Cluster.controlFor(world, instantiator, properties, localNodeProperties);

    instance = new NodeBootstrap(control, localNodeProperties);

    control._2.info("Successfully started cluster node: '" + localNodeProperties + "'");

    if (!embedded) {
      control._2.info("==========");
    }

    return instance;
  }

  public ClusterControl clusterControl() {
    return clusterControl._1;
  }

  private NodeBootstrap(final Tuple2<ClusterControl, Logger> control, final String nodeName) throws Exception {
    this.clusterControl = control;

    ShutdownHook shutdownHook = new ShutdownHook(nodeName, control);
    shutdownHook.register();
  }
}

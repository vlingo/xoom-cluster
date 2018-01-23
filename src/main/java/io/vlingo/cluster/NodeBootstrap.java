// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster;

import io.vlingo.actors.Logger;
import io.vlingo.cluster.model.Cluster;
import io.vlingo.cluster.model.ClusterSnapshotControl;
import io.vlingo.cluster.model.Properties;
import io.vlingo.common.fn.Tuple2;

public final class NodeBootstrap {
  private static NodeBootstrap instance;

  private final Tuple2<ClusterSnapshotControl, Logger> clusterSnapshotControl;
  private final ShutdownHook shutdownHook;

  public static void main(final String[] args) throws Exception {
    if (args.length == 1) {
      boot(args[0]);
    } else if (args.length > 1) {
      System.err.println("vlingo/cluster: Too many arguments; provide node name only.");
      System.exit(1);
    } else {
      System.err.println("vlingo/cluster: This node must be named with a command-line argument.");
      System.exit(1);
    }
  }

  public static NodeBootstrap boot(final String nodeName) throws Exception {
    return boot(nodeName, false);
  }

  public static NodeBootstrap boot(final String nodeName, final boolean embedded) throws Exception {
    final boolean mustBoot = NodeBootstrap.instance == null || !Cluster.isRunning();
    
    if (mustBoot) {
      Properties.instance.validateRequired(nodeName);
      
      final Tuple2<ClusterSnapshotControl, Logger> control = Cluster.controlFor(nodeName);
      
      NodeBootstrap.instance = new NodeBootstrap(control, nodeName);
      
      control._2.log("vlingo/cluster: Successfully started cluster node: '" + nodeName + "'");
      
      if (!embedded) {
        control._2.log("==========");
      }
    }

    return NodeBootstrap.instance;
  }

  public static NodeBootstrap instance() {
    return instance;
  }
  
  public ClusterSnapshotControl clusterSnapshotControl() {
    return clusterSnapshotControl._1;
  }

  private NodeBootstrap(final Tuple2<ClusterSnapshotControl, Logger> control, final String nodeName) throws Exception {
    this.clusterSnapshotControl = control;

    this.shutdownHook = new ShutdownHook(nodeName, control);
    this.shutdownHook.register();
  }
}

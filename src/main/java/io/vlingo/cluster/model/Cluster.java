// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Logger;
import io.vlingo.actors.World;
import io.vlingo.common.fn.Tuple2;

public class Cluster {
  
  private static final World world = World.start("vlingo-cluster");
  
  private static ClusterSnapshotControl control = null;
  
  public static final synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(final String name) throws Exception {
    if (control != null) {
      throw new IllegalArgumentException("Cluster snapshot control already exists.");
    }
    
    final Tuple2<ClusterSnapshotControl, Logger> control = ClusterSnapshotControl.instance(world, name);
    
    Cluster.control = control._1;
    
    return control;
  }

  public static boolean isRunning() {
    return control != null;
  }

  protected static final synchronized void reset() {
    control = null;
  }
}

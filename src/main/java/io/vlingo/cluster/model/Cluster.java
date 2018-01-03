// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.World;

public class Cluster {
  
  private static final World world = World.start("vlingo-cluster");
  
  private static ClusterSnapshotControl control = null;
  
  public static final synchronized ClusterSnapshotControl controlFor(final String name) throws Exception {
    if (control != null) {
      throw new IllegalArgumentException("Cluster snapshot control already exists.");
    }
    
    control = ClusterSnapshotControl.instance(world, name);
    
    return control;
  }

  public static boolean isRunning() {
    return control != null;
  }

  protected static final synchronized void reset() {
    control = null;
  }
}

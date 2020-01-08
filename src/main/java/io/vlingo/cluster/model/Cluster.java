// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.concurrent.atomic.AtomicReference;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.application.ClusterApplication.ClusterApplicationInstantiator;
import io.vlingo.common.Tuple2;

public class Cluster {

  private static AtomicReference<ClusterSnapshotControl> control = new AtomicReference<>();
  private static World world;

  public static final synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName)
  throws Exception {

    if (world != null) {
      throw new IllegalArgumentException("Cluster snapshot control already exists.");
    }
    return controlFor(World.start("vlingo-cluster"), instantiator, properties, nodeName);
  }

  public static final synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(
          final World world,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName)
  throws Exception {
    return controlFor(world, world.stage(), instantiator, properties, nodeName);
  }

  public static final synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(
          final World world,
          final Stage stage,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName)
  throws Exception {

    if (control.get() != null) {
      throw new IllegalArgumentException("Cluster snapshot control already exists.");
    }

    Cluster.world = world;

    final Tuple2<ClusterSnapshotControl, Logger> control = ClusterSnapshotControl.instance(world, instantiator, properties, nodeName);

    Cluster.control.set(control._1);

    return control;
  }

  public static boolean isRunning() {
    return control.get() != null;
  }

  public static boolean isRunning(final boolean expected, final int retries) {
    for (int idx = 0; idx < retries; ++idx) {
      if (isRunning() == expected) {
        return expected;
      }
      try { Thread.sleep(500); } catch (Exception e) { }
    }
    return !expected;
  }

  public static final synchronized void reset() {
    world = null;
    control.set(null);
  }

  static class ClusterSnapshotActorInstantiator implements ActorInstantiator<ClusterSnapshotActor> {
    private final ClusterApplication clusterApplication;
    private final ClusterSnapshotInitializer initializer;

    public ClusterSnapshotActorInstantiator(final ClusterSnapshotInitializer initializer, final ClusterApplication clusterApplication) {
      this.initializer = initializer;
      this.clusterApplication = clusterApplication;
    }

    @Override
    public ClusterSnapshotActor instantiate() {
      try {
        return new ClusterSnapshotActor(initializer, clusterApplication);
      } catch (Exception e) {
        throw new IllegalArgumentException("Failed to instantiate " + type() + " because: " + e.getMessage(), e);
      }
    }

    @Override
    public Class<ClusterSnapshotActor> type() {
      return ClusterSnapshotActor.class;
    }
  }
}

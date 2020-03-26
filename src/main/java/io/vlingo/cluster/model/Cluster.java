// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.UUID;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.application.ClusterApplication.ClusterApplicationInstantiator;
import io.vlingo.common.Tuple2;

public class Cluster {
  static final String INTERNAL_NAME = UUID.randomUUID().toString();

  public static synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName)
  throws Exception {
    return controlFor(World.start("vlingo-cluster"), instantiator, properties, nodeName);
  }

  public static synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(
          final World world,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName)
  throws Exception {
    return controlFor(world, world.stage(), instantiator, properties, nodeName);
  }

  public static synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(
          final World world,
          final Stage stage,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName)
  throws Exception {

    if (isRunningInside(world)) {
      throw new IllegalArgumentException("Cluster is already running inside World: " + world.name());
    }

    final Tuple2<ClusterSnapshotControl, Logger> control = ClusterSnapshotControl.instance(world, instantiator, properties, nodeName);

    world.registerDynamic(INTERNAL_NAME, control._1);

    return control;
  }

  public static boolean isRunningInside(final World world) {
    return world.resolveDynamic(INTERNAL_NAME, ClusterSnapshotControl.class) != null;
  }

  static class ClusterSnapshotActorInstantiator implements ActorInstantiator<ClusterSnapshotActor> {
    private static final long serialVersionUID = 6105119774787607965L;

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

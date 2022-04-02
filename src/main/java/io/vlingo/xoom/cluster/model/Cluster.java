// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.UUID;

import io.vlingo.xoom.actors.ActorInstantiator;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.cluster.model.application.ClusterApplication2.ClusterApplicationInstantiator;
import io.vlingo.xoom.cluster.model.application.ClusterApplication2;
import io.vlingo.xoom.common.Tuple2;

public class Cluster {
  static final String INTERNAL_NAME = UUID.randomUUID().toString();

  public static synchronized Tuple2<ClusterControl, Logger> controlFor(
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName) {
    return controlFor(World.start("xoom-cluster"), instantiator, properties, nodeName);
  }

  public static synchronized Tuple2<ClusterControl, Logger> controlFor(
          final World world,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName) {
    return controlFor(world, world.stage(), instantiator, properties, nodeName);
  }

  public static synchronized Tuple2<ClusterControl, Logger> controlFor(
          final World world,
          final Stage stage,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName) {

    if (isRunningInside(world)) {
      throw new IllegalArgumentException("Cluster is already running inside World: " + world.name());
    }

    final Tuple2<ClusterControl, Logger> control = ClusterControl.instance(world, instantiator, properties, nodeName);

    world.registerDynamic(INTERNAL_NAME, control._1);

    return control;
  }

  public static boolean isRunningInside(final World world) {
    return world.resolveDynamic(INTERNAL_NAME, ClusterControl.class) != null;
  }

  static class ClusterActorInstantiator implements ActorInstantiator<ClusterActor> {
    private static final long serialVersionUID = 6105119774787607965L;

    private final ClusterApplication2 clusterApplication;
    private final ClusterInitializer initializer;

    public ClusterActorInstantiator(final ClusterInitializer initializer, final ClusterApplication2 clusterApplication) {
      this.initializer = initializer;
      this.clusterApplication = clusterApplication;
    }

    @Override
    public ClusterActor instantiate() {
      try {
        return new ClusterActor(initializer, clusterApplication);
      } catch (Exception e) {
        throw new IllegalArgumentException("Failed to instantiate " + type() + " because: " + e.getMessage(), e);
      }
    }

    @Override
    public Class<ClusterActor> type() {
      return ClusterActor.class;
    }
  }
}

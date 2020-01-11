// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.application.ClusterApplication.ClusterApplicationInstantiator;
import io.vlingo.common.Tuple2;

public interface ClusterSnapshotControl {
  public static Tuple2<ClusterSnapshotControl, Logger> instance(
          final World world,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName) {

    final String stageName = properties.clusterApplicationStageName();
    final Stage stage = world.stageNamed(stageName);

    return instance(world, stage, instantiator, properties, nodeName);
  }

  public static Tuple2<ClusterSnapshotControl, Logger> instance(
          final World world,
          final Stage stage,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final String nodeName) {

    final ClusterSnapshotInitializer initializer = new ClusterSnapshotInitializer(nodeName, properties, world.defaultLogger());
    instantiator.node(initializer.localNode());

    final ClusterApplication application = ClusterApplication.instance(stage, instantiator);

    final Definition definition =
            new Definition(
                    ClusterSnapshotActor.class,
                    new ClusterSnapshotInstantiator(initializer, application),
                    "cluster-snapshot-" + nodeName);

    ClusterSnapshotControl control = world.actorFor(ClusterSnapshotControl.class, definition);

    return Tuple2.from(control, world.defaultLogger());
  }

  void shutDown();

  static class ClusterSnapshotInstantiator implements ActorInstantiator<ClusterSnapshotActor> {
    final ClusterApplication application;
    final ClusterSnapshotInitializer initializer;

    public ClusterSnapshotInstantiator(final ClusterSnapshotInitializer initializer, final ClusterApplication application) {
      this.initializer = initializer;
      this.application = application;
    }

    @Override
    public ClusterSnapshotActor instantiate() {
      try {
        return new ClusterSnapshotActor(initializer, application);
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

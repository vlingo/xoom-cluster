// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.common.Tuple2;

public interface ClusterControl {
  void shutDown();

  static Tuple2<ClusterControl, Logger> instance(
      final World world,
      final ClusterApplication.ClusterApplicationInstantiator<?> instantiator,
      final Properties properties,
      final String localNodeProperties) {

    final String stageName = properties.clusterApplicationStageName();
    final Stage stage = world.stageNamed(stageName);

    return instance(world, stage, instantiator, properties, localNodeProperties);
  }

  static Tuple2<ClusterControl, Logger> instance(
      final World world,
      final Stage stage,
      final ClusterApplication.ClusterApplicationInstantiator<?> instantiator,
      final Properties properties,
      final String localNodeProperties) {

    final ClusterInitializer initializer = new ClusterInitializer(localNodeProperties, properties, world.defaultLogger());
    instantiator.node(initializer.localNode());
    instantiator.registry(initializer.registry());

    final ClusterApplication application = ClusterApplication.instance(stage, instantiator);

    final Definition definition =
        new Definition(
            ClusterActor.class,
            new ClusterControl.ClusterInstantiator(initializer, application),
            "cluster-actor-" + initializer.localNode().name().value());

    ClusterControl control = world.actorFor(ClusterControl.class, definition);

    return Tuple2.from(control, world.defaultLogger());
  }

  class ClusterInstantiator implements ActorInstantiator<ClusterActor> {
    private static final long serialVersionUID = -5766576644564817563L;

    final ClusterApplication application;
    final ClusterInitializer initializer;

    public ClusterInstantiator(final ClusterInitializer initializer, final ClusterApplication application) {
      this.initializer = initializer;
      this.application = application;
    }

    @Override
    public ClusterActor instantiate() {
      try {
        return new ClusterActor(initializer, application);
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

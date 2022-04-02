package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.cluster.model.application.ClusterApplication2;
import io.vlingo.xoom.common.Tuple2;

public interface ClusterControl {
  void shutDown();

  static Tuple2<ClusterControl, Logger> instance(
      final World world,
      final ClusterApplication2.ClusterApplicationInstantiator<?> instantiator,
      final Properties properties,
      final String nodeName) {

    final String stageName = properties.clusterApplicationStageName();
    final Stage stage = world.stageNamed(stageName);

    return instance(world, stage, instantiator, properties, nodeName);
  }

  static Tuple2<ClusterControl, Logger> instance(
      final World world,
      final Stage stage,
      final ClusterApplication2.ClusterApplicationInstantiator<?> instantiator,
      final Properties properties,
      final String nodeName) {

    final ClusterInitializer initializer = new ClusterInitializer(nodeName, properties, world.defaultLogger());
    instantiator.node(initializer.localNode());

    final ClusterApplication2 application = ClusterApplication2.instance(stage, instantiator);

    final Definition definition =
        new Definition(
            ClusterSnapshotActor.class,
            new ClusterControl.ClusterInstantiator(initializer, application),
            "cluster-snapshot-" + nodeName);

    ClusterControl control = world.actorFor(ClusterControl.class, definition);

    return Tuple2.from(control, world.defaultLogger());
  }

  class ClusterInstantiator implements ActorInstantiator<ClusterActor> {
    private static final long serialVersionUID = -5766576644564817563L;

    final ClusterApplication2 application;
    final ClusterInitializer initializer;

    public ClusterInstantiator(final ClusterInitializer initializer, final ClusterApplication2 application) {
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

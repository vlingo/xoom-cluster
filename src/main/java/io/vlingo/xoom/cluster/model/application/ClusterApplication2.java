package io.vlingo.xoom.cluster.model.application;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Node;

public interface ClusterApplication2 extends Startable, Stoppable, ClusterContextAware, ClusterAttributesAware {
  void informResponder(final ApplicationOutboundStream responder);
  void handleApplicationMessage(final RawMessage message);

  static ClusterApplication2 instance(
      final World world,
      final ClusterApplication2.ClusterApplicationInstantiator<?> instantiator,
      final Properties properties,
      final Node node) {

    final Stage applicationStage =
        world.stageNamed(properties.clusterApplicationStageName());

    return applicationStage.actorFor(
        ClusterApplication2.class,
        Definition.has(instantiator.type(), instantiator, "cluster-application"));
  }

  static <A extends Actor> ClusterApplication2 instance(final Stage applicationStage, final ActorInstantiator<A> instantator) {
    return applicationStage.actorFor(
        ClusterApplication2.class,
        Definition.has(instantator.type(), instantator, "cluster-application"));
  }

  abstract class ClusterApplicationInstantiator<A extends Actor> implements ActorInstantiator<A> {
    private static final long serialVersionUID = -2002705648453794614L;

    private Node node;
    private final Class<A> type;

    public ClusterApplicationInstantiator(final Class<A> type) {
      this.type = type;
    }

    public Node node() {
      return this.node;
    }

    public void node(final Node node) {
      this.node = node;
    }

    @Override
    public Class<A> type() {
      return type;
    }
  }

  class DefaultClusterApplicationInstantiator extends ClusterApplicationInstantiator<FakeClusterApplicationActor> {
    private static final long serialVersionUID = -4275208628986318945L;

    public DefaultClusterApplicationInstantiator() {
      super(FakeClusterApplicationActor.class);
    }

    @Override
    public FakeClusterApplicationActor instantiate() {
      return new FakeClusterApplicationActor(node());
    }
  }
}

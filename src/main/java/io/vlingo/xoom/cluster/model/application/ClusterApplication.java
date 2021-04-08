// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.application;

import java.util.Collection;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorInstantiator;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.Startable;
import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.cluster.model.attribute.AttributesProtocol;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public interface ClusterApplication extends Startable, Stoppable {
  static ClusterApplication instance(
          final World world,
          final ClusterApplicationInstantiator<?> instantiator,
          final Properties properties,
          final Node node) {

    final Stage applicationStage =
            world.stageNamed(properties.clusterApplicationStageName());

    return applicationStage.actorFor(
            ClusterApplication.class,
            Definition.has(instantiator.type(), instantiator, "cluster-application"));
  }

  static <A extends Actor> ClusterApplication instance(final Stage applicationStage, final ActorInstantiator<A> instantator) {
    return applicationStage.actorFor(
            ClusterApplication.class,
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

  void handleApplicationMessage(final RawMessage message);

  void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster);
  void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading);
  void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster);
  void informLocalNodeShutDown(final Id nodeId);
  void informLocalNodeStarted(final Id nodeId);
  void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster);
  void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster);
  void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster);
  void informQuorumAchieved();
  void informQuorumLost();
  void informResponder(final ApplicationOutboundStream responder);

  void informAttributesClient(final AttributesProtocol client);
  void informAttributeSetCreated(final String attributeSetName);
  void informAttributeAdded(final String attributeSetName, final String attributeName);
  void informAttributeRemoved(final String attributeSetName, final String attributeName);
  void informAttributeSetRemoved(final String attributeSetName);
  void informAttributeReplaced(final String attributeSetName, final String attributeName);
}

// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import io.vlingo.xoom.actors.ActorInstantiator;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.cluster.model.ClusterSnapshot;
import io.vlingo.xoom.cluster.model.message.OperationalMessage;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.wire.node.Configuration;
import io.vlingo.xoom.wire.node.Node;
import io.vlingo.xoom.wire.node.NodeSynchronizer;

public interface LocalLiveNode extends Stoppable {
  static LocalLiveNode instance(
      final Stage stage,
      final Node node,
      final ClusterSnapshot snapshot,
      final Registry registry,
      final OperationalOutboundStream outbound,
      final Configuration configuration) {

    final Definition definition =
            new Definition(
                    LocalLiveNodeActor.class,
                    new LocalLiveNodeInstantiator(node, snapshot, registry, outbound, configuration),
                    "local-live-node");

    return stage.actorFor(LocalLiveNode.class, definition);
  }

  class LocalLiveNodeInstantiator implements ActorInstantiator<LocalLiveNodeActor> {
    private static final long serialVersionUID = -1254550990754440942L;

    private final Node node;
    private final ClusterSnapshot snapshot;
    private final io.vlingo.xoom.cluster.model.node.Registry registry;
    private final OperationalOutboundStream outbound;
    private final Configuration configuration;

    public LocalLiveNodeInstantiator(
            final Node node,
            final ClusterSnapshot snapshot,
            final io.vlingo.xoom.cluster.model.node.Registry registry,
            final OperationalOutboundStream outbound,
            final Configuration configuration) {
      this.node = node;
      this.snapshot = snapshot;
      this.registry = registry;
      this.outbound = outbound;
      this.configuration = configuration;
    }

    @Override
    public LocalLiveNodeActor instantiate() {
      return new LocalLiveNodeActor(node, snapshot, registry, outbound, configuration);
    }

    @Override
    public Class<LocalLiveNodeActor> type() {
      return LocalLiveNodeActor.class;
    }
  }

  void handle(final OperationalMessage message);
  void registerNodeSynchronizer(final NodeSynchronizer nodeSynchronizer);
}

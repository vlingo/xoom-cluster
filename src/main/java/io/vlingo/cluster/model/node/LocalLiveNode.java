// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Stoppable;
import io.vlingo.cluster.model.ClusterSnapshot;
import io.vlingo.cluster.model.message.OperationalMessage;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.wire.node.Configuration;
import io.vlingo.wire.node.Node;
import io.vlingo.wire.node.NodeSynchronizer;

public interface LocalLiveNode extends Stoppable {
  public static LocalLiveNode instance(
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

    LocalLiveNode localLiveNode = stage.actorFor(LocalLiveNode.class, definition);

    return localLiveNode;
  }

  static class LocalLiveNodeInstantiator implements ActorInstantiator<LocalLiveNodeActor> {
    private static final long serialVersionUID = -1254550990754440942L;

    private final Node node;
    private final ClusterSnapshot snapshot;
    private final io.vlingo.cluster.model.node.Registry registry;
    private final OperationalOutboundStream outbound;
    private final Configuration configuration;

    public LocalLiveNodeInstantiator(
            final Node node,
            final ClusterSnapshot snapshot,
            final io.vlingo.cluster.model.node.Registry registry,
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

// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.common.Scheduled;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.node.Node;
import io.vlingo.xoom.wire.node.NodeSynchronizer;

public interface AttributesAgent extends AttributesCommands, NodeSynchronizer, InboundStreamInterest, Scheduled<Object>, Stoppable {
  static AttributesAgent instance(
      final Stage stage,
      final Node localNode,
      final ClusterApplication application,
      final OperationalOutboundStream outbound,
      final Registry registry,
      final Logger logger) {

    final Definition definition =
            new Definition(
                    AttributesAgentActor.class,
                    new AttributesAgentInstantiator(localNode, application, outbound, registry, logger),
                    "attributes-agent");

    return stage.actorFor(AttributesAgent.class, definition);
  }

  class AttributesAgentInstantiator implements ActorInstantiator<AttributesAgentActor> {
    private static final long serialVersionUID = 3269867041246996465L;

    private final Node localNode;
    private final ClusterApplication application;
    private final OperationalOutboundStream outbound;
    private final Registry registry;
    private final Logger logger;

    public AttributesAgentInstantiator(
            final Node localNode,
            final ClusterApplication application,
            final OperationalOutboundStream outbound,
            final Registry registry,
            final Logger logger) {
      this.localNode = localNode;
      this.application = application;
      this.outbound = outbound;
      this.registry = registry;
      this.logger = logger;
    }

    @Override
    public AttributesAgentActor instantiate() {
      return new AttributesAgentActor(localNode, application, outbound, registry, logger);
    }

    @Override
    public Class<AttributesAgentActor> type() {
      return AttributesAgentActor.class;
    }
  }
}

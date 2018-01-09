// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Scheduled;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Stoppable;
import io.vlingo.cluster.model.Configuration;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.inbound.InboundStreamInterest;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.cluster.model.node.NodeSynchronizer;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;

public interface AttributesAgent extends AttributesProtocol, NodeSynchronizer, InboundStreamInterest, Scheduled, Stoppable {
  public static AttributesAgent instance(
          final Stage stage,
          final Node node,
          final ClusterApplication application,
          final OperationalOutboundStream outbound,
          final Configuration configuration) {
    
    final Definition definition =
            new Definition(
                    AttributesAgentActor.class,
                    Definition.parameters(node, application, outbound, configuration),
                    "attributes-agent");
    
    AttributesAgent attributesAgent = stage.actorFor(definition, AttributesAgent.class);
    
    return attributesAgent;
  }
}

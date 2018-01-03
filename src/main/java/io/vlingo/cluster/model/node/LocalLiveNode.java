// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Stoppable;
import io.vlingo.cluster.model.ClusterSnapshot;
import io.vlingo.cluster.model.Configuration;
import io.vlingo.cluster.model.message.OperationalMessage;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;

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
                    Definition.parameters(node, snapshot, registry, outbound, configuration),
                    "local-live-node");
    
    LocalLiveNode localLiveNode = stage.actorFor(definition, LocalLiveNode.class);
    
    return localLiveNode;
  }
  public static LocalLiveNode instance() {
    return null;
  }
  
  void handle(final OperationalMessage message);
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Stoppable;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.outbound.ApplicationOutboundStreamActor;
import io.vlingo.cluster.model.outbound.ManagedOutboundChannelProvider;
import io.vlingo.common.message.ByteBufferPool;
import io.vlingo.common.message.RawMessage;

public interface ClusterApplicationOutboundStream extends Stoppable {
  public static ClusterApplicationOutboundStream instance(
          final Stage stage,
          final ManagedOutboundChannelProvider provider,
          final ByteBufferPool byteBufferPool) {
    
    final Definition definition =
            Definition.has(
                    ApplicationOutboundStreamActor.class,
                    Definition.parameters(provider, byteBufferPool),
                    "cluster-application-outbound-stream");
    
    final ClusterApplicationOutboundStream clusterApplicationOutboundStream =
            stage.actorFor(definition, ClusterApplicationOutboundStream.class);
    
    return clusterApplicationOutboundStream;
  }
  
  void broadcast(final RawMessage message);
  void sendTo(final RawMessage message, final Id targetId);
}

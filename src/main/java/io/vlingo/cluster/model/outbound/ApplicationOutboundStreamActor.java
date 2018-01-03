// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import io.vlingo.actors.Actor;
import io.vlingo.cluster.model.application.ClusterApplicationOutboundStream;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.common.message.ByteBufferPool;
import io.vlingo.common.message.RawMessage;

public class ApplicationOutboundStreamActor extends Actor
  implements ClusterApplicationOutboundStream {

  private final Outbound outbound;
  
  public ApplicationOutboundStreamActor(
          final ManagedOutboundChannelProvider provider,
          final ByteBufferPool byteBufferPool) {
    
    this.outbound = new Outbound(provider, byteBufferPool);
  }

  //===================================
  // ClusterApplicationResponder
  //===================================

  @Override
  public void broadcast(final RawMessage message) {
    outbound.broadcast(message);
  }

  @Override
  public void sendTo(final RawMessage message, final Id targetId) {
    outbound.sendTo(message, targetId);
  }

  //===================================
  // Stoppable
  //===================================
  
  public void stop() {
    outbound.close();
    
    super.stop();
  }
}

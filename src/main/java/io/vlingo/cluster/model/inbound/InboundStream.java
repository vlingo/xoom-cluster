// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.nio.ByteBuffer;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Startable;
import io.vlingo.actors.Stoppable;
import io.vlingo.cluster.model.node.AddressType;

public interface InboundStream extends Startable, Stoppable {
  public static InboundStream instance(
          final Stage stage,
          final InboundStreamInterest interest,
          final int port,
          final AddressType addressType,
          final String inboundName,
          final int maxMessageSize)
  throws Exception {
    
    final InboundReader reader =
            new SocketChannelInboundReader(port, inboundName, maxMessageSize);
    
    final Definition definition =
            Definition.has(
                    InboundStreamActor.class,
                    Definition.parameters(interest, addressType, reader),
                    "cluster-" + inboundName + "-inbound");
    
    final InboundStream inboundStream = stage.actorFor(definition, InboundStream.class);
    
    return inboundStream;
  }

  void respondWith(final InboundClientReference clientReference, final ByteBuffer buffer) throws Exception;
}

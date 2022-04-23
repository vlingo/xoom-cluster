// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.cluster.model.message.*;
import io.vlingo.xoom.common.pool.ResourcePool;
import io.vlingo.xoom.wire.fdx.outbound.ManagedOutboundChannelProvider;
import io.vlingo.xoom.wire.fdx.outbound.Outbound;
import io.vlingo.xoom.wire.message.ConsumerByteBuffer;
import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class OperationalOutboundStreamActor extends Actor
  implements OperationalOutboundStream {

  private static final Logger logger = LoggerFactory.getLogger(
      OperationalOutboundStreamActor.class);

  private final Node node;
  private final Outbound outbound;

  public OperationalOutboundStreamActor(
          final Node node,
          final ManagedOutboundChannelProvider provider,
          final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {

    this.node = node;
    this.outbound = new Outbound(provider, byteBufferPool);
  }


  //===================================
  // OperationalOutbound
  //===================================

  @Override
  public void close(final Id id) {
    logger.debug("Closing Id: {}", id);
    outbound.close(id);
  }

  @Override
  public void application(final ApplicationSays says, final Collection<Node> unconfirmedNodes) {
    final ConsumerByteBuffer buffer = outbound.lendByteBuffer();
    MessageConverters.messageToBytes(says, buffer.asByteBuffer());

    final RawMessage message = Converters.toRawMessage(node.id().value(), buffer.asByteBuffer());

    logger.debug("Broadcasting ApplicationSays {} to {}", says.saysId, debug(unconfirmedNodes));
    outbound.broadcast(unconfirmedNodes, outbound.bytesFrom(message, buffer));
  }

  private <E> String debug(Collection<E> collection) {
    if (logger.isDebugEnabled()) return "";
    return String.format("[%s]", collection.stream().map(Object::toString).collect(Collectors.joining(", ")));
  }


  //===================================
  // Stoppable
  //===================================

  @Override
  public void stop() {
    logger.debug("Stopping...");
    outbound.close();

    super.stop();
  }
}

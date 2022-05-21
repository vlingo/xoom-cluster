// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.transport.api.Message;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.cluster.model.message.ApplicationSays;
import io.vlingo.xoom.cluster.model.message.MessageConverters;
import io.vlingo.xoom.common.pool.ResourcePool;
import io.vlingo.xoom.wire.message.ConsumerByteBuffer;
import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Collection;

public class OperationalOutboundStreamActor extends Actor
  implements OperationalOutboundStream {

  private static final Logger logger = LoggerFactory.getLogger(
      OperationalOutboundStreamActor.class);

  private final Cluster cluster;
  private final Node node;
  private final ResourcePool<ConsumerByteBuffer, String> byteBufferPool;

  public OperationalOutboundStreamActor(
          final Cluster cluster,
          final Node node,
          final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {

    this.cluster = cluster;
    this.node = node;
    this.byteBufferPool = byteBufferPool;
  }

  @Override
  public void close(final Id id) {
    logger.debug("Closing Id: {}", id);
  }

  @Override
  public void application(final ApplicationSays says, final Collection<Node> unconfirmedNodes) {
    logger.debug("Broadcasting ApplicationSays {}", says.saysId);
    final byte[] messageBytes = bytesFrom(says);

    cluster.spreadGossip(Message.withData(messageBytes).build())
            .doOnError(throwable -> logger.error("Failed to spread gossip because of " + throwable.getMessage(), throwable))
            .doOnSuccess(val -> logger.debug("Successfully spread gossip: " + says))
            .subscribe(val -> logger.debug("Spread gossip with " + val));
  }

  //===================================
  // Stoppable
  //===================================

  @Override
  public void stop() {
    logger.debug("Stopping...");
    super.stop();
  }

  public byte[] bytesFrom(final ApplicationSays says) {
    final ConsumerByteBuffer buffer = byteBufferPool.acquire("Outbound#lendByteBuffer");
    MessageConverters.messageToBytes(says, buffer.asByteBuffer());
    final RawMessage message = Converters.toRawMessage(node.id().value(), buffer.asByteBuffer());
    message.copyBytesTo(buffer.clear().asByteBuffer());
    final ConsumerByteBuffer flipped = buffer.flip();
    final ByteBuffer readBuffer = ByteBuffer.wrap(flipped.array(), flipped.position(), flipped.limit())
            .asReadOnlyBuffer()
            .order(flipped.order());
    final byte[] messageBytes = new byte[readBuffer.remaining()];
    readBuffer.get(messageBytes);

    return messageBytes;
  }
}

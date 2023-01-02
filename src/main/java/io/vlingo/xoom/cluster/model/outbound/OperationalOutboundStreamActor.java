// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.Member;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.cluster.model.message.ApplicationSays;
import io.vlingo.xoom.cluster.model.message.MessageConverters;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.common.Scheduled;
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
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class OperationalOutboundStreamActor extends Actor implements OperationalOutboundStream, Scheduled<Object> {

  private static final Logger logger = LoggerFactory.getLogger(OperationalOutboundStreamActor.class);

  private final Cluster cluster;
  private final Registry registry;
  private final ResourcePool<ConsumerByteBuffer, String> byteBufferPool;

  private final AtomicBoolean scheduled = new AtomicBoolean(false);
  private final Queue<Runnable> outMessages;

  public OperationalOutboundStreamActor(
          final Cluster cluster,
          final Registry registry,
          final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {

    this.cluster = cluster;
    this.registry = registry;
    this.byteBufferPool = byteBufferPool;
    this.outMessages = new ConcurrentLinkedQueue<>();
  }

  @Override
  public void close(final Id id) {
    logger.debug("Closing Id: {}", id);
  }

  @Override
  public void application(final ApplicationSays says, final Collection<Node> unconfirmedNodes) {
    Runnable send = () -> {
      logger.debug("Sending ApplicationSays {}", says.saysId);
      final byte[] messageBytes = bytesFrom(says);
      for (Node node : unconfirmedNodes) {
        Address nodeAddress = Address.create(node.operationalAddress().hostName(), node.operationalAddress().port());
        Optional<Member> maybeMember = cluster.member(nodeAddress);
        if (maybeMember.isPresent()) {
          cluster.send(maybeMember.get(), Message.withData(messageBytes).build())
                  .doOnError(throwable -> logger.error("Failed to send message because of " + throwable.getMessage(), throwable))
                  .doOnSuccess(val -> logger.debug("Successfully sent the message: " + says))
                  .subscribe(val -> logger.debug("Message sent with " + val));
        } else {
          logger.error("Failed to find node " + node.name() + " in the cluster!");
        }
      }
    };

    if (registry.isClusterHealthy()) {
      send.run();
    } else {
      logger.debug("Buffering ApplicationSays {}", says.saysId);
      outMessages.offer(send);
      if (scheduled.compareAndSet(false, true)) {
        logger.debug("Scheduling ApplicationSays {}", says.saysId);
        stage().scheduler()
                .scheduleOnce(selfAs(Scheduled.class), null, 0L, 500L);
      }
    }
  }

  @Override
  public void application(ApplicationSays says) {
    logger.debug("Broadcasting ApplicationSays {}", says.saysId);
    final byte[] messageBytes = bytesFrom(says);

    cluster.spreadGossip(Message.withData(messageBytes).build())
            .doOnError(throwable -> logger.error("Failed to spread gossip because of " + throwable.getMessage(), throwable))
            .doOnSuccess(val -> logger.debug("Successfully spread gossip: " + says))
            .subscribe(val -> logger.debug("Spread gossip with " + val));
  }

  @Override
  public void intervalSignal(Scheduled<Object> scheduled, Object o) {
    if (registry.isClusterHealthy()) {
      this.scheduled.set(false);
      Runnable outMessage;
      while ((outMessage = outMessages.peek()) != null) {
        outMessage.run();
      }
    } else {
      if (!this.scheduled.getAndSet(true)) {
        logger.debug("Rescheduling the sending of buffered messages");
        stage().scheduler()
                .scheduleOnce(selfAs(Scheduled.class), null, 0L, 500L);
      }
    }
  }

  //===================================
  // Stoppable
  //===================================

  @Override
  public void stop() {
    logger.debug("Stopping...");
    super.stop();
  }

  private byte[] bytesFrom(final ApplicationSays says) {
    final ConsumerByteBuffer buffer = byteBufferPool.acquire("Outbound#lendByteBuffer");
    MessageConverters.messageToBytes(says, buffer.asByteBuffer());
    final RawMessage message = Converters.toRawMessage(registry.localNode().id().value(), buffer.asByteBuffer());
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

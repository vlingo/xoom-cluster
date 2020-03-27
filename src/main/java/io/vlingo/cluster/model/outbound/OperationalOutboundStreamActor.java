// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import io.vlingo.actors.Actor;
import io.vlingo.cluster.model.message.*;
import io.vlingo.common.pool.ResourcePool;
import io.vlingo.wire.fdx.outbound.ManagedOutboundChannelProvider;
import io.vlingo.wire.fdx.outbound.Outbound;
import io.vlingo.wire.message.ConsumerByteBuffer;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class OperationalOutboundStreamActor extends Actor
  implements OperationalOutboundStream {

  private static final Logger logger = LoggerFactory.getLogger(
      OperationalOutboundStreamActor.class);

  private final OperationalMessageCache cache;
  private final Node node;
  private final Outbound outbound;

  public OperationalOutboundStreamActor(
          final Node node,
          final ManagedOutboundChannelProvider provider,
          final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {

    this.node = node;
    this.outbound = new Outbound(provider, byteBufferPool);
    this.cache = new OperationalMessageCache(node);
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

  @Override
  public void directory(final Set<Node> allLiveNodes) {
    final Directory dir = new Directory(node.id(), node.name(), allLiveNodes);

    final ConsumerByteBuffer buffer = outbound.lendByteBuffer();
    MessageConverters.messageToBytes(dir, buffer.asByteBuffer());

    final RawMessage message = Converters.toRawMessage(node.id().value(), buffer.asByteBuffer());

    logger.debug("Broadcasting directory {}", debug(allLiveNodes));
    outbound.broadcast(outbound.bytesFrom(message, buffer));
  }

  @Override
  public void elect(final Collection<Node> allGreaterNodes) {
    logger.debug("Broadcasting ellect {}", debug(allGreaterNodes));
    outbound.broadcast(allGreaterNodes, cache.cachedRawMessage(OperationalMessage.ELECT));
  }

  @Override
  public void join() {
    logger.debug("Broadcasting join");
    outbound.broadcast(cache.cachedRawMessage(OperationalMessage.JOIN));
  }

  @Override
  public void leader() {
    logger.debug("Broadcasting leader");
    outbound.broadcast(cache.cachedRawMessage(OperationalMessage.LEADER));
  }

  @Override
  public void leader(final Id id) {
    logger.debug("Broadcasting leader Id: {}", id);
    outbound.sendTo(cache.cachedRawMessage(OperationalMessage.LEADER), id);
  }

  @Override
  public void leave() {
    logger.debug("Broadcasting leave");
    outbound.broadcast(cache.cachedRawMessage(OperationalMessage.LEAVE));
  }

  @Override
  public void open(final Id id) {
    logger.debug("open Id: {}", id);
    outbound.open(id);
  }

  @Override
  public void ping(final Id targetNodeId) {
    logger.debug("Sending ping to: {}", targetNodeId);
    outbound.sendTo(cache.cachedRawMessage(OperationalMessage.PING), targetNodeId);
  }

  @Override
  public void pulse(final Id targetNodeId) {
    logger.debug("Sending pulse to: {}", targetNodeId);
    outbound.sendTo(cache.cachedRawMessage(OperationalMessage.PULSE), targetNodeId);
  }

  @Override
  public void pulse() {
    logger.debug("Broadcasting pulse");
    outbound.broadcast(cache.cachedRawMessage(OperationalMessage.PULSE));
  }

  @Override
  public void split(final Id targetNodeId, final Id currentLeaderId) {
    final Split split = new Split(currentLeaderId);

    final ConsumerByteBuffer buffer = outbound.lendByteBuffer();
    MessageConverters.messageToBytes(split, buffer.asByteBuffer());

    final RawMessage message = Converters.toRawMessage(node.id().value(), buffer.asByteBuffer());

    logger.debug("Sending split: {} to: {}", split, currentLeaderId);
    outbound.sendTo(outbound.bytesFrom(message, buffer), targetNodeId);
  }

  @Override
  public void vote(final Id targetNodeId) {
    logger.debug("Sending vote to: {}", targetNodeId);
    outbound.sendTo(cache.cachedRawMessage(OperationalMessage.VOTE), targetNodeId);
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

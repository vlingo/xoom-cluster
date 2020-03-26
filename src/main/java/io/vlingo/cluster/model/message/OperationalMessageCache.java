// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Node;

public class OperationalMessageCache {
  private final Map<String, RawMessage> messages;
  private final Node node;

  public OperationalMessageCache(final Node node) {
    this.messages = new HashMap<>();
    this.node = node;

    cacheValidTypes();
  }

  public RawMessage cachedRawMessage(final String type) {
    final RawMessage rawMessage = messages.get(type);

    if (rawMessage == null) {
      throw new IllegalArgumentException("Cache does not support type: '" + type + "'");
    }

    return rawMessage;
  }

  private void cacheValidTypes() {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(1000);

    cacheElect(buffer);
    cacheJoin(buffer);
    cacheLeader(buffer);
    cacheLeave(buffer);
    cachePing(buffer);
    cachePulse(buffer);
    cacheVote(buffer);
  }

  private void cacheElect(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Elect(node.id()), buffer);
    cacheMessagePair(buffer, OperationalMessage.ELECT);
  }

  private void cacheJoin(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Join(node), buffer);
    cacheMessagePair(buffer, OperationalMessage.JOIN);
  }

  private void cacheLeader(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Leader(node.id()), buffer);
    cacheMessagePair(buffer, OperationalMessage.LEADER);
  }

  private void cacheLeave(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Leave(node.id()), buffer);
    cacheMessagePair(buffer, OperationalMessage.LEAVE);
  }

  private void cachePing(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Ping(node.id()), buffer);
    cacheMessagePair(buffer, OperationalMessage.PING);
  }

  private void cachePulse(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Pulse(node.id()), buffer);
    cacheMessagePair(buffer, OperationalMessage.PULSE);
  }

  private void cacheVote(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Vote(node.id()), buffer);
    cacheMessagePair(buffer, OperationalMessage.VOTE);
  }

  private void cacheMessagePair(final ByteBuffer buffer, final String typeKey) {
    final RawMessage cachedMessage = Converters.toRawMessage(node.id().value(), buffer);
    messages.put(typeKey, cachedMessage);
  }
}

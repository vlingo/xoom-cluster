// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.common.message.Converters;
import io.vlingo.common.message.RawMessage;

public class OperationalMessageCache {
  private final Map<String, RawMessage> messages;
  private final Id id;
  private final Name name;

  public OperationalMessageCache(final Id id, final Name name) {
    this.messages = new HashMap<String, RawMessage>();
    this.id = id;
    this.name = name;

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
    final ByteBuffer buffer = ByteBuffer.allocate(1000);

    cacheElect(buffer);
    cacheJoin(buffer);
    cacheLeader(buffer);
    cacheLeave(buffer);
    cachePing(buffer);
    cachePulse(buffer);
    cacheVote(buffer);
  }

  private void cacheElect(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Elect(id), buffer);
    cacheMessagePair(buffer, OperationalMessage.ELECT);
  }

  private void cacheJoin(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Join(Node.from(id, name)), buffer);
    cacheMessagePair(buffer, OperationalMessage.JOIN);
  }

  private void cacheLeader(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Leader(id), buffer);
    cacheMessagePair(buffer, OperationalMessage.LEADER);
  }

  private void cacheLeave(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Leave(id), buffer);
    cacheMessagePair(buffer, OperationalMessage.LEAVE);
  }

  private void cachePing(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Ping(id), buffer);
    cacheMessagePair(buffer, OperationalMessage.PING);
  }

  private void cachePulse(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Pulse(id), buffer);
    cacheMessagePair(buffer, OperationalMessage.PULSE);
  }

  private void cacheVote(final ByteBuffer buffer) {
    MessageConverters.messageToBytes(new Vote(id), buffer);
    cacheMessagePair(buffer, OperationalMessage.VOTE);
  }

  private void cacheMessagePair(final ByteBuffer buffer, final String typeKey) {
    final RawMessage cachedMessage = Converters.toRawMessage(id.value(), buffer);
    messages.put(typeKey, cachedMessage);
  }
}

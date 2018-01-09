// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.common.message.ByteBufferPool;
import io.vlingo.common.message.ByteBufferPool.PooledByteBuffer;
import io.vlingo.common.message.RawMessage;

public class Outbound {
  private final ByteBufferPool pool;
  private final ManagedOutboundChannelProvider provider;

  protected Outbound(
      final ManagedOutboundChannelProvider provider,
      final ByteBufferPool byteBufferPool) {

    this.provider = provider;
    this.pool = byteBufferPool;
  }

  protected void broadcast(final RawMessage message) {
    final PooledByteBuffer buffer = pool.access();
    broadcast(bytesFrom(message, buffer));
  }

  protected void broadcast(final PooledByteBuffer buffer) {
    // currently based on configured nodes,
    // but eventually could be live-node based
    broadcast(provider.allOtherNodeChannels(), buffer);
  }

  protected void broadcast(final Collection<Node> selectNodes, final RawMessage message) {
    final PooledByteBuffer buffer = pool.access();
    broadcast(selectNodes, bytesFrom(message, buffer));
  }

  protected void broadcast(final Collection<Node> selectNodes, final PooledByteBuffer buffer) {
    broadcast(provider.channelsFor(selectNodes), buffer);
  }

  protected PooledByteBuffer bytesFrom(final RawMessage message, final PooledByteBuffer buffer) {
    final ByteBuffer copyBuffer = buffer.buffer();
    copyBuffer.clear();
    message.copyBytesTo(copyBuffer);
    copyBuffer.flip();
    return buffer;
  }

  protected void close() {
    provider.close();
  }

  protected void close(final Id id) {
    provider.close(id);
  }

  protected void open(final Id id) {
    provider.channelFor(id);
  }

  protected final PooledByteBuffer pooledByteBuffer() {
    return pool.access();
  }

  protected void sendTo(final RawMessage message, final Id id) {
    final PooledByteBuffer buffer = pool.access();
    sendTo(bytesFrom(message, buffer), id);
  }

  protected void sendTo(final PooledByteBuffer buffer, final Id id) {
    try {
      open(id);
      provider.channelFor(id).write(buffer.buffer());
    } finally {
      buffer.release();
    }
  }

  private void broadcast(final Map<Id, ManagedOutboundChannel> channels, final PooledByteBuffer buffer) {
    try {
      final ByteBuffer bufferToWrite = buffer.buffer();
      for (final ManagedOutboundChannel channel: channels.values()) {
        bufferToWrite.position(0);
        channel.write(bufferToWrite);
      }
    } finally {
      buffer.release();
    }
  }
}

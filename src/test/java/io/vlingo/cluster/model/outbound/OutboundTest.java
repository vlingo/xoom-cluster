// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.common.pool.ElasticResourcePool;
import io.vlingo.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.wire.fdx.outbound.Outbound;
import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.ConsumerByteBuffer;
import io.vlingo.wire.message.ConsumerByteBufferPool;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

public class OutboundTest extends AbstractClusterTest {
  private static final String Message1 = "Message1";
  private static final String Message2 = "Message2";
  private static final String Message3 = "Message3";

  private MockManagedOutboundChannelProvider channelProvider;
  private ConsumerByteBufferPool pool;
  private Outbound outbound;

  @Test
  public void testBroadcast() throws Exception {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(properties.operationalBufferSize());

    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, Message1);
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer, Message2);
    final RawMessage rawMessage3 = buildRawMessageBuffer(buffer, Message3);

    outbound.broadcast(rawMessage1);
    outbound.broadcast(rawMessage2);
    outbound.broadcast(rawMessage3);

    for (final ManagedOutboundChannel channel : channelProvider.allOtherNodeChannels().values()) {
      final MockManagedOutboundChannel mock = (MockManagedOutboundChannel) channel;

      assertEquals(Message1, mock.writes.get(0));
      assertEquals(Message2, mock.writes.get(1));
      assertEquals(Message3, mock.writes.get(2));
    }
  }

  @Test
  public void testBroadcastPooledByteBuffer() throws Exception {
    final ConsumerByteBuffer buffer1 = pool.acquire();
    final ConsumerByteBuffer buffer2 = pool.acquire();
    final ConsumerByteBuffer buffer3 = pool.acquire();

    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer1.asByteBuffer(), Message1);
    bytesFrom(rawMessage1, buffer1.asByteBuffer());
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer2.asByteBuffer(), Message2);
    bytesFrom(rawMessage2, buffer2.asByteBuffer());
    final RawMessage rawMessage3 = buildRawMessageBuffer(buffer3.asByteBuffer(), Message3);
    bytesFrom(rawMessage3, buffer3.asByteBuffer());

    outbound.broadcast(buffer1);
    outbound.broadcast(buffer2);
    outbound.broadcast(buffer3);

    for (final ManagedOutboundChannel channel : channelProvider.allOtherNodeChannels().values()) {
      final MockManagedOutboundChannel mock = (MockManagedOutboundChannel) channel;

      assertEquals(Message1, mock.writes.get(0));
      assertEquals(Message2, mock.writes.get(1));
      assertEquals(Message3, mock.writes.get(2));
    }
  }

  @Test
  public void testBroadcastToSelectNodes() throws Exception {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(properties.operationalBufferSize());

    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, Message1);
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer, Message2);
    final RawMessage rawMessage3 = buildRawMessageBuffer(buffer, Message3);

    final List<Node> selectNodes = asList(config.nodeMatching(Id.of(3)));

    outbound.broadcast(selectNodes, rawMessage1);
    outbound.broadcast(selectNodes, rawMessage2);
    outbound.broadcast(selectNodes, rawMessage3);

    final MockManagedOutboundChannel mock = (MockManagedOutboundChannel) channelProvider.channelFor(Id.of(3));

    assertEquals(Message1, mock.writes.get(0));
    assertEquals(Message2, mock.writes.get(1));
    assertEquals(Message3, mock.writes.get(2));
  }

  @Test
  public void testSendTo() throws Exception {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(properties.operationalBufferSize());

    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, Message1);
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer, Message2);
    final RawMessage rawMessage3 = buildRawMessageBuffer(buffer, Message3);

    final Id id3 = Id.of(3);

    outbound.sendTo(rawMessage1, id3);
    outbound.sendTo(rawMessage2, id3);
    outbound.sendTo(rawMessage3, id3);

    final MockManagedOutboundChannel mock = (MockManagedOutboundChannel) channelProvider.channelFor(Id.of(3));

    assertEquals(Message1, mock.writes.get(0));
    assertEquals(Message2, mock.writes.get(1));
    assertEquals(Message3, mock.writes.get(2));
  }

  @Test
  public void testSendToPooledByteBuffer() throws Exception {
    final ConsumerByteBuffer buffer1 = pool.acquire();
    final ConsumerByteBuffer buffer2 = pool.acquire();
    final ConsumerByteBuffer buffer3 = pool.acquire();

    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer1.asByteBuffer(), Message1);
    bytesFrom(rawMessage1, buffer1.asByteBuffer());
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer2.asByteBuffer(), Message2);
    bytesFrom(rawMessage2, buffer2.asByteBuffer());
    final RawMessage rawMessage3 = buildRawMessageBuffer(buffer3.asByteBuffer(), Message3);
    bytesFrom(rawMessage3, buffer3.asByteBuffer());

    final Id id3 = Id.of(3);

    outbound.sendTo(buffer1, id3);
    outbound.sendTo(buffer2, id3);
    outbound.sendTo(buffer3, id3);

    final MockManagedOutboundChannel mock = (MockManagedOutboundChannel) channelProvider.channelFor(Id.of(3));

    assertEquals(Message1, mock.writes.get(0));
    assertEquals(Message2, mock.writes.get(1));
    assertEquals(Message3, mock.writes.get(2));
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    pool = new ConsumerByteBufferPool(ElasticResourcePool.Config.of(10), properties.applicationBufferSize());
    channelProvider = new MockManagedOutboundChannelProvider(Id.of(1), config);
    outbound = new Outbound(channelProvider, pool);
  }
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.cluster.model.outbound.ManagedOutboundSocketChannel;
import io.vlingo.common.message.RawMessage;

public class InboundSocketChannelTest extends AbstractClusterTest {
  private static final String AppMessage = "APP TEST ";
  private static final String OpMessage = "OP TEST ";
  
  private ManagedOutboundSocketChannel appChannel;
  private InboundReader appReader;
  private ManagedOutboundSocketChannel opChannel;
  private InboundReader opReader;
  private Node node;
  
  @Test
  public void testOpInboundChannel() throws Exception {
    final MockInboundReaderConsumer consumer = new MockInboundReaderConsumer();
    
    opReader.openFor(consumer);
    
    final ByteBuffer buffer = ByteBuffer.allocate(properties.operationalBufferSize());
    
    final String message1 = OpMessage + 1;
    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, message1);
    opChannel.write(bytesFrom(rawMessage1, buffer));
    
    probeUntilConsumed(opReader, consumer);
    
    assertEquals(1, consumer.consumeCount);
    assertEquals(message1, consumer.messages.get(0));

    final String message2 = OpMessage + 2;
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer, message2);
    opChannel.write(bytesFrom(rawMessage2, buffer));
    
    probeUntilConsumed(opReader, consumer);
    
    assertEquals(2, consumer.consumeCount);
    assertEquals(message2, consumer.messages.get(1));
  }
  
  @Test
  public void testAppInboundChannel() throws Exception {
    final MockInboundReaderConsumer consumer = new MockInboundReaderConsumer();
    
    appReader.openFor(consumer);
    
    final ByteBuffer buffer = ByteBuffer.allocate(properties.applicationBufferSize());
    
    final String message1 = AppMessage + 1;
    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, message1);
    appChannel.write(bytesFrom(rawMessage1, buffer));
    
    probeUntilConsumed(appReader, consumer);
    
    assertEquals(1, consumer.consumeCount);
    assertEquals(message1, consumer.messages.get(0));

    final String message2 = AppMessage + 2;
    final RawMessage rawMessage2 = buildRawMessageBuffer(buffer, message2);
    appChannel.write(bytesFrom(rawMessage2, buffer));
    
    probeUntilConsumed(appReader, consumer);
    
    assertEquals(2, consumer.consumeCount);
    assertEquals(message2, consumer.messages.get(1));
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    node = config.configuredNodeMatching(Id.of(2));
    opChannel = new ManagedOutboundSocketChannel(node, node.operationalAddress(), testWorld.defaultLogger());
    appChannel = new ManagedOutboundSocketChannel(node, node.applicationAddress(), testWorld.defaultLogger());
    opReader = new SocketChannelInboundReader(node.operationalAddress().port(), "test-op", properties.operationalBufferSize(), testWorld.defaultLogger());
    appReader = new SocketChannelInboundReader(node.applicationAddress().port(), "test-app", properties.applicationBufferSize(), testWorld.defaultLogger());
  }
  
  @After
  public void tearDown() {
    opChannel.close();
    appChannel.close();
    opReader.close();
    appReader.close();
  }

  private void probeUntilConsumed(final InboundReader reader, final MockInboundReaderConsumer consumer) {
    final int currentConsumedCount = consumer.consumeCount;
    
    for (int idx = 0; idx < 100; ++idx) {
      reader.probeChannel();
      
      if (consumer.consumeCount > currentConsumedCount) break;
    }
  }
}

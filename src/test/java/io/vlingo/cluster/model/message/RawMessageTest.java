// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.Test;

import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.message.RawMessageHeader;
import io.vlingo.wire.node.Host;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class RawMessageTest extends AbstractClusterTest {

  @Test
  public void testKnownSizeWithAppend() {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(1000);
    final Node node1 = nextNodeWith(1);
    final Join join = new Join(node1);
    MessageConverters.messageToBytes(join, buffer);
    buffer.flip();
    final int messageSize = buffer.limit();
    final RawMessage message = new RawMessage(messageSize); // known size
    message.header(RawMessageHeader.from(node1.id().value(), 0, messageSize));
    message.append(buffer.array(), 0, messageSize);
    
    assertEquals(node1.id().value(), message.header().nodeId());
    assertEquals(messageSize, message.header().length());
    assertEquals(join, OperationalMessage.messageFrom(message.asTextMessage()));
  }

  @Test
  public void testFromBytesWithLengthAndRequiredMessageLength() {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(1000);
    final Node node1 = nextNodeWith(1);
    final Join join = new Join(node1);
    MessageConverters.messageToBytes(join, buffer);
    buffer.flip();
    final int messageSize = buffer.limit();
    final byte[] messageBytes = new byte[messageSize];
    System.arraycopy(buffer.array(), 0, messageBytes, 0, messageSize);
    final RawMessage message = new RawMessage(messageBytes);
    message.header(RawMessageHeader.from(node1.id().value(), 0, message.length()));
    
    assertEquals(node1.id().value(), message.header().nodeId());
    assertEquals(message.length(), message.header().length());
    assertEquals(message.length(), message.requiredMessageLength());
    assertEquals(join, OperationalMessage.messageFrom(message.asTextMessage()));
  }

  @Test
  public void testCopyBytesTo() {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(1000);
    final Node node1 = nextNodeWith(1);
    final Join join = new Join(node1);
    MessageConverters.messageToBytes(join, buffer);
    buffer.flip();
    final int messageSize = buffer.limit();
    final byte[] messageBytes = new byte[messageSize];
    System.arraycopy(buffer.array(), 0, messageBytes, 0, messageSize);
    final RawMessage message = new RawMessage(messageBytes);
    message.header(RawMessageHeader.from(node1.id().value(), 0, message.length()));
    
    buffer.clear();
    message.copyBytesTo(buffer); // copyBytesTo
    final String text = Converters.bytesToText(buffer.array(), RawMessageHeader.BYTES, message.length());
    assertTrue(OperationalMessage.messageFrom(text).isJoin());
  }

  @Test
  public void testHeaderFrom() {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(1000);
    final RawMessageHeader header = RawMessageHeader.from(1, 0, 100);
    final RawMessage message = new RawMessage(100);
    header.copyBytesTo(buffer);
    buffer.flip();
    message.headerFrom(buffer);
    buffer.clear();
    final RawMessageHeader convertedHeader = RawMessageHeader.from(buffer);
    assertEquals(header, convertedHeader);
  }

  @Test
  public void testPut() {
    final ByteBuffer buffer = ByteBufferAllocator.allocate(1000);
    final Node node1 = Node.with(Id.of(1), Name.of("node1"), Host.of("localhost"), 37371, 37372);
    final Join join = new Join(node1);
    MessageConverters.messageToBytes(join, buffer);
    final RawMessage message = new RawMessage(1000);
    message.put(buffer);
    buffer.position(0);
    final String textOfRawMessage = message.asTextMessage();
    final String textOfConvertedBuffer = Converters.bytesToText(buffer.array(), 0, buffer.limit());
    assertEquals(textOfConvertedBuffer, textOfRawMessage);
  }
}

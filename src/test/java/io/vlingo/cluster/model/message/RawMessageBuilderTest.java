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

import org.junit.Before;
import org.junit.Test;

import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.message.RawMessageBuilder;
import io.vlingo.wire.message.RawMessageHeader;
import io.vlingo.wire.node.Host;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class RawMessageBuilderTest {
  private ByteBuffer buffer;
  private RawMessageBuilder builder;
  private Join join;
  private Leader leader;
  private Node node1;
  private Node node2;

  @Test
  public void testOneInboundMessage() {
    join();
    
    builder.prepareContent().sync();
    assertTrue(builder.isCurrentMessageComplete());
    final RawMessage message = builder.currentRawMessage();
    final String text = message.asTextMessage();
    final OperationalMessage typed = OperationalMessage.messageFrom(text);
    assertEquals(join, typed);
  }

  @Test
  public void testTwoInboundMessages() {
    join();
    leader();
    
    builder.prepareContent().sync();
    assertTrue(builder.isCurrentMessageComplete());
    assertTrue(builder.hasContent());
    final RawMessage inboundJoin = builder.currentRawMessage();
    final String joinText = inboundJoin.asTextMessage();
    final OperationalMessage joinFromText = OperationalMessage.messageFrom(joinText);
    assertEquals(join, joinFromText);
    
    builder.prepareForNextMessage();
    assertTrue(builder.hasContent());
    builder.sync();
    
    final RawMessage inboundLeader = builder.currentRawMessage();
    final String leaderText = inboundLeader.asTextMessage();
    final OperationalMessage leaderFromText = OperationalMessage.messageFrom(leaderText);
    assertEquals(leader, leaderFromText);
  }

//  @Test
//  public void testTwoInboundMessagesWithPartialRead() {
//    join();
//    leaderFirstHalf();
//    
//    builder.prepareContent().sync();
//    final RawMessage inboundJoin = builder.currentRawMessage();
//    final String joinText = inboundJoin.asTextMessage();
//    System.out.println("RAW MESSAGE: " + joinText);
//    final OperationalMessage joinFromText = OperationalMessage.messageFrom(joinText);
//    assertEquals(join, joinFromText);
//    
//    leaderSecondHalf();
//    builder.prepareForNextMessage();
//    builder.sync();
//    
//    final RawMessage inboundLeader = builder.currentRawMessage();
//    final String leaderText = inboundLeader.asTextMessage();
//    System.out.println("RAW MESSAGE: " + leaderText);
//    final OperationalMessage inboundFromText = OperationalMessage.messageFrom(leaderText);
//    assertEquals(leader, inboundFromText);
//  }

  @Before
  public void setUp() {
    buffer = ByteBufferAllocator.allocate(1000);
    builder = new RawMessageBuilder(1000);
    node1 = Node.with(Id.of(1), Name.of("node1"), Host.of("localhost"), 37371, 37372);
    node2 = Node.with(Id.of(2), Name.of("node2"), Host.of("localhost"), 37373, 37374);
    join = new Join(node1);
    leader = new Leader(node2.id());
  }

  private void join() {
    buffer.clear();
    
    MessageConverters.messageToBytes(join, buffer);
    
    buffer.flip();
    
    prepareRawMessage(0, buffer.limit());
  }

  private void leader() {
    buffer.clear();
    
    MessageConverters.messageToBytes(leader, buffer);
    
    buffer.flip();
    
    prepareRawMessage(0, buffer.limit());
  }

//  private void leaderFirstHalf() {
//    buffer.clear();
//    builder.prepareForNextMessage();
//    
//    Converters.messageToBytes(leader, buffer);
//    
//    buffer.flip();
//    
//    prepareRawMessage(0, buffer.limit() / 2);
//  }
//
//  private void leaderSecondHalf() {
//    buffer.clear();
//    builder.prepareForNextMessage();
//    
//    Converters.messageToBytes(leader, buffer);
//    
//    buffer.flip();
//    
//    prepareRawMessage(buffer.limit() / 2, buffer.limit() % 2);
//  }

  private void prepareRawMessage(final int position, final int bytesToAppend) {
    final RawMessageHeader inboundHeader = new RawMessageHeader(node1.id().value(), (short) 0, (short) buffer.limit());
    final RawMessage inboundMessage = new RawMessage(buffer.limit());
    if (position == 0) {
      inboundMessage.header(inboundHeader);
    }
    inboundMessage.append(buffer.array(), position, bytesToAppend);
    inboundMessage.copyBytesTo(builder.workBuffer());
  }
}

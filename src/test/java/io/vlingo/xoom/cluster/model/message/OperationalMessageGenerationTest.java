// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.wire.message.ByteBufferAllocator;
import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.node.Address;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;

public class OperationalMessageGenerationTest {
  private static final int BYTES = 4096;
  private ByteBuffer expectedBuffer = ByteBufferAllocator.allocate(BYTES);
  private ByteBuffer messageBuffer = ByteBufferAllocator.allocate(BYTES);

  @Test
  public void testGenerateApplicationSaidMessage() {
    final Id id = Id.of(1);
    final Name name = new Name("node1");
    final String payload = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sagittis risus quis nulla blandit, a euismod massa egestas. Vivamus facilisis.";

    final ApplicationSays app = ApplicationSays.from(id, name, payload);
    final String raw = OperationalMessage.APP + "\n" + "id=1 nm=node1 si=" + app.saysId + "\n" + payload;
    expectedBuffer.put(Converters.textToBytes(raw));
    MessageConverters.messageToBytes(app, messageBuffer);
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(app, ApplicationSays.from(raw));
  }

  @Test
  public void testGenerateDirectoryMessge() {
    final String[] opAddresses = { "", "localhost:47471", "localhost:47473", "localhost:47475" };
    final String[] appAddresses = { "", "localhost:47472", "localhost:47474", "localhost:47476" };

    Set<Node> nodeEntries = new HashSet<Node>();
    nodeEntries.add(new Node(
        Id.of(1),
        new Name("node1"),
        Address.from(opAddresses[1], AddressType.OP),
        Address.from(appAddresses[1], AddressType.APP)));
    nodeEntries.add(new Node(
        Id.of(2),
        new Name("node2"),
        Address.from(opAddresses[2], AddressType.OP),
        Address.from(appAddresses[2], AddressType.APP)));
    nodeEntries.add(new Node(
        Id.of(3),
        new Name("node3"),
        Address.from(opAddresses[3], AddressType.OP),
        Address.from(appAddresses[3], AddressType.APP)));
    Directory dir = new Directory(Id.of(1), new Name("node1"), nodeEntries);
    MessageConverters.messageToBytes(dir, messageBuffer);
    final String raw =
            OperationalMessage.DIR + "\n"
                    + "id=1 nm=node1\n"
                    + "id=1 nm=node1 op=" + opAddresses[1] + " app=" + appAddresses[1] + "\n"
                    + "id=2 nm=node2 op=" + opAddresses[2] + " app=" + appAddresses[2] + "\n"
                    + "id=3 nm=node3 op=" + opAddresses[3] + " app=" + appAddresses[3];
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(dir, Directory.from(raw));
  }

  @Test
  public void testGenerateElectMessage() {
    Elect elect = new Elect(Id.of(1));
    MessageConverters.messageToBytes(elect, messageBuffer);
    final String raw = OperationalMessage.ELECT + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(elect, Elect.from(raw));
  }

  @Test
  public void testGenerateJoinMessge() {
    Join join = new Join(new Node(
        Id.of(1),
        new Name("node1"),
        Address.from("localhost:47471", AddressType.OP),
        Address.from("localhost:47472", AddressType.APP)));
    MessageConverters.messageToBytes(join, messageBuffer);
    final String raw = OperationalMessage.JOIN + "\n" + "id=1 nm=node1 op=localhost:47471 app=localhost:47472";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(join, Join.from(raw));
  }

  @Test
  public void testGenerateLeaderMessage() {
    Leader leader = new Leader(Id.of(1));
    MessageConverters.messageToBytes(leader, messageBuffer);
    final String raw = OperationalMessage.LEADER + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(leader, Leader.from(raw));
  }

  @Test
  public void testGenerateLeaveMessage() {
    Leave leave = new Leave(Id.of(1));
    MessageConverters.messageToBytes(leave, messageBuffer);
    final String raw = OperationalMessage.LEAVE + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(leave, Leave.from(raw));
  }

  @Test
  public void testGeneratePingMessage() {
    Ping ping = new Ping(Id.of(1));
    MessageConverters.messageToBytes(ping, messageBuffer);
    final String raw = OperationalMessage.PING + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(ping, Ping.from(raw));
  }

  @Test
  public void testGeneratePulseMessage() {
    Pulse pulse = new Pulse(Id.of(1));
    MessageConverters.messageToBytes(pulse, messageBuffer);
    final String raw = OperationalMessage.PULSE + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(pulse, Pulse.from(raw));
  }

  @Test
  public void testGenerateSplitMessage() {
    Split split = new Split(Id.of(1));
    MessageConverters.messageToBytes(split, messageBuffer);
    final String raw = OperationalMessage.SPLIT + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(split, Split.from(raw));
  }

  @Test
  public void testGenerateVoteMessage() {
    Vote vote = new Vote(Id.of(1));
    MessageConverters.messageToBytes(vote, messageBuffer);
    final String raw = OperationalMessage.VOTE + "\nid=1";
    expectedBuffer.put(Converters.textToBytes(raw));
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(vote, Vote.from(raw));
  }

  @Before
  public void setUp() {
    expectedBuffer.clear();
    messageBuffer.clear();

    for (int idx = 0; idx < BYTES; ++idx) {
      expectedBuffer.put(idx, (byte) 0);
      messageBuffer.put(idx, (byte) 0);
    }
  }
}

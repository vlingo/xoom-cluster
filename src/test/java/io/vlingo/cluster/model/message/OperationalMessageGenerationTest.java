// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.cluster.model.node.Address;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.common.message.Converters;

public class OperationalMessageGenerationTest {
  private static final int BYTES = 4096;
  private ByteBuffer expectedBuffer = ByteBuffer.allocate(BYTES);
  private ByteBuffer messageBuffer = ByteBuffer.allocate(BYTES);

  @Test
  public void testGenerateDirectoryMessge() {
    final String[] opAddresses = { "", "localhost:37371", "localhost:37373", "localhost:37375" };
    final String[] appAddresses = { "", "localhost:37372", "localhost:37374", "localhost:37376" };

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
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.DIR + "\n"
        + "id=1 nm=node1\n"
        + "id=1 nm=node1 op=" + opAddresses[1] + " msg=" + appAddresses[1] + "\n"
        + "id=2 nm=node2 op=" + opAddresses[2] + " msg=" + appAddresses[2] + "\n"
        + "id=3 nm=node3 op=" + opAddresses[3] + " msg=" + appAddresses[3]));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGenerateElectMessage() {
    Elect elect = new Elect(Id.of(1));
    MessageConverters.messageToBytes(elect, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.ELECT + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGenerateJoinMessge() {
    Join join = new Join(new Node(
        Id.of(1),
        new Name("node1"),
        Address.from("localhost:37371", AddressType.OP),
        Address.from("localhost:37372", AddressType.APP)));
    MessageConverters.messageToBytes(join, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.JOIN + "\n" + "id=1 nm=node1 op=localhost:37371 msg=localhost:37372"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGenerateLeaderMessage() {
    Leader leader = new Leader(Id.of(1));
    MessageConverters.messageToBytes(leader, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.LEADER + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGenerateLeaveMessage() {
    Leave leave = new Leave(Id.of(1));
    MessageConverters.messageToBytes(leave, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.LEAVE + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGeneratePingMessage() {
    Ping ping = new Ping(Id.of(1));
    MessageConverters.messageToBytes(ping, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.PING + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGeneratePulseMessage() {
    Pulse pulse = new Pulse(Id.of(1));
    MessageConverters.messageToBytes(pulse, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.PULSE + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGenerateSplitMessage() {
    Split split = new Split(Id.of(1));
    MessageConverters.messageToBytes(split, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.SPLIT + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
  }

  @Test
  public void testGenerateVoteMessage() {
    Vote vote = new Vote(Id.of(1));
    MessageConverters.messageToBytes(vote, messageBuffer);
    expectedBuffer.put(Converters.textToBytes(OperationalMessage.VOTE + "\nid=1"));
    assertEquals(expectedBuffer, messageBuffer);
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

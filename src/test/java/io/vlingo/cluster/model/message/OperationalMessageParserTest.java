// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class OperationalMessageParserTest {

  @Test
  public void testParseDirectory() {
    OperationalMessage dir = OperationalMessage.messageFrom(MessageFixtures.directoryAsText(1, 2, 3));

    assertEquals(true, dir.isDirectory());
    assertEquals(Id.of(MessageFixtures.defaultNodeId()), dir.id());
    assertEquals(new Name(MessageFixtures.defaultNodeName()), ((Directory) dir).name());

    int index = 1;
    for (Node node : ((Directory) dir).nodes()) {
      assertEquals(Id.of(index), node.id());
      assertEquals(new Name("node"+index), node.name());
      assertEquals(Address.from(MessageFixtures.opAddress(index), AddressType.OP), node.operationalAddress());
      assertEquals(Address.from(MessageFixtures.appAddress(index), AddressType.APP), node.applicationAddress());

      ++index;
    }

    Set<Node> nodeEntries = new HashSet<Node>();
    nodeEntries.add(new Node(
        Id.of(1),
        new Name("node1"),
        Address.from(MessageFixtures.opAddress(1), AddressType.OP),
        Address.from(MessageFixtures.appAddress(1), AddressType.APP)));
    nodeEntries.add(new Node(
        Id.of(2),
        new Name("node2"),
        Address.from(MessageFixtures.opAddress(2), AddressType.OP),
        Address.from(MessageFixtures.appAddress(2), AddressType.APP)));
    nodeEntries.add(new Node(
        Id.of(3),
        new Name("node3"),
        Address.from(MessageFixtures.opAddress(3), AddressType.OP),
        Address.from(MessageFixtures.appAddress(3), AddressType.APP)));

    Directory expectedDir = new Directory(Id.of(MessageFixtures.defaultNodeId()), new Name(MessageFixtures.defaultNodeName()), nodeEntries);

    assertEquals(expectedDir, dir);
  }

  @Test
  public void testParseElect() {
    OperationalMessage elect1 = OperationalMessage.messageFrom(OperationalMessage.ELECT + "\n" + "id=1");
    assertEquals(true, elect1.isElect());
    assertEquals(Id.of(1), elect1.id());
    Elect expectedElec1 = new Elect(Id.of(1));
    assertEquals(expectedElec1, elect1);

    OperationalMessage elect100 = OperationalMessage.messageFrom(OperationalMessage.ELECT + "\n" + "id=100");
    assertEquals(true, elect100.isElect());
    assertEquals(Id.of(100), elect100.id());
    Elect expectedElec100 = new Elect(Id.of(100));
    assertEquals(expectedElec100, elect100);
  }

  @Test
  public void testParseJoin() {
    OperationalMessage join = OperationalMessage.messageFrom(MessageFixtures.joinAsText());
    assertEquals(true, join.isJoin());
    Node expectedNode =
        new Node(
            Id.of(1),
            new Name("node1"),
            Address.from("localhost:37371", AddressType.OP),
            Address.from("localhost:37372", AddressType.APP));
    Join expectedJoin = new Join(expectedNode);
    assertEquals(expectedNode, ((Join) join).node());
    assertEquals(expectedJoin, join);
  }

  @Test
  public void testParseLeader() {
    OperationalMessage leader1 = OperationalMessage.messageFrom(MessageFixtures.leaderAsText());
    assertEquals(true, leader1.isLeader());
    assertEquals(Id.of(1), leader1.id());
    Leader expectedLeader1 = new Leader(Id.of(1));
    assertEquals(expectedLeader1, leader1);

    OperationalMessage leader100 = OperationalMessage.messageFrom(OperationalMessage.LEADER + "\n" + "id=100");
    assertEquals(true, leader100.isLeader());
    assertEquals(Id.of(100), leader100.id());
    Leader expectedLeader100 = new Leader(Id.of(100));
    assertEquals(expectedLeader100, leader100);
  }

  @Test
  public void testParseLeave() {
    OperationalMessage leave1 = OperationalMessage.messageFrom(MessageFixtures.leaveAsText());
    assertEquals(true, leave1.isLeave());
    assertEquals(Id.of(1), leave1.id());
    Leave expectedLeave1 = new Leave(Id.of(1));
    assertEquals(expectedLeave1, leave1);

    OperationalMessage leave100 = OperationalMessage.messageFrom(OperationalMessage.LEAVE + "\n" + "id=100");
    assertEquals(true, leave100.isLeave());
    assertEquals(Id.of(100), leave100.id());
    Leave expectedLeave100 = new Leave(Id.of(100));
    assertEquals(expectedLeave100, leave100);
  }

  @Test
  public void testParsePing() {
    OperationalMessage ping1 = OperationalMessage.messageFrom(OperationalMessage.PING + "\n" + "id=1");
    assertEquals(true, ping1.isPing());
    assertEquals(Id.of(1), ping1.id());
    Ping expectedPing1 = new Ping(Id.of(1));
    assertEquals(expectedPing1, ping1);

    OperationalMessage ping100 = OperationalMessage.messageFrom(OperationalMessage.PING + "\n" + "id=100");
    assertEquals(true, ping100.isPing());
    assertEquals(Id.of(100), ping100.id());
    Ping expectedPing100 = new Ping(Id.of(100));
    assertEquals(expectedPing100, ping100);
  }

  @Test
  public void testParsePulse() {
    OperationalMessage pulse1 = OperationalMessage.messageFrom(OperationalMessage.PULSE + "\n" + "id=1");
    assertEquals(true, pulse1.isPulse());
    assertEquals(Id.of(1), pulse1.id());
    Pulse expectedPulse1 = new Pulse(Id.of(1));
    assertEquals(expectedPulse1, pulse1);

    OperationalMessage pulse100 = OperationalMessage.messageFrom(OperationalMessage.PULSE + "\n" + "id=100");
    assertEquals(true, pulse100.isPulse());
    assertEquals(Id.of(100), pulse100.id());
    Pulse expectedPulse100 = new Pulse(Id.of(100));
    assertEquals(expectedPulse100, pulse100);
  }

  @Test
  public void testParseVote() {
    OperationalMessage vote1 = OperationalMessage.messageFrom(OperationalMessage.VOTE + "\n" + "id=1");
    assertEquals(true, vote1.isVote());
    assertEquals(Id.of(1), vote1.id());
    Vote expectedVote1 = new Vote(Id.of(1));
    assertEquals(expectedVote1, vote1);

    OperationalMessage vote100 = OperationalMessage.messageFrom(OperationalMessage.VOTE + "\n" + "id=100");
    assertEquals(true, vote100.isVote());
    assertEquals(Id.of(100), vote100.id());
    Vote expectedVote100 = new Vote(Id.of(100));
    assertEquals(expectedVote100, vote100);
  }
}

// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Host;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;

public class OperationalMessageCacheTest {

  @Test
  public void testCachedMessages() {
    final Node node1 = Node.with(Id.of(2), Name.of("node2"), Host.of("localhost"), 37373, 37374);
    final OperationalMessageCache cache = new OperationalMessageCache(node1);
    
    final RawMessage elect = cache.cachedRawMessage(OperationalMessage.ELECT);
    assertTrue(OperationalMessage.messageFrom(elect.asTextMessage()).isElect());
    
    final RawMessage join = cache.cachedRawMessage(OperationalMessage.JOIN);
    assertTrue(OperationalMessage.messageFrom(join.asTextMessage()).isJoin());
    
    final RawMessage leader = cache.cachedRawMessage(OperationalMessage.LEADER);
    assertTrue(OperationalMessage.messageFrom(leader.asTextMessage()).isLeader());
    
    final RawMessage leave = cache.cachedRawMessage(OperationalMessage.LEAVE);
    assertTrue(OperationalMessage.messageFrom(leave.asTextMessage()).isLeave());
    
    final RawMessage ping = cache.cachedRawMessage(OperationalMessage.PING);
    assertTrue(OperationalMessage.messageFrom(ping.asTextMessage()).isPing());
    
    final RawMessage pulse = cache.cachedRawMessage(OperationalMessage.PULSE);
    assertTrue(OperationalMessage.messageFrom(pulse.asTextMessage()).isPulse());
    
    final RawMessage vote = cache.cachedRawMessage(OperationalMessage.VOTE);
    assertTrue(OperationalMessage.messageFrom(vote.asTextMessage()).isVote());
  }

  @Test
  public void testNonCachedMessages() {
    final Node node1 = Node.with(Id.of(2), Name.of("node2"), Host.of("localhost"), 37373, 37374);
    final OperationalMessageCache cache = new OperationalMessageCache(node1);
    
    boolean caught;
    
    try {
      caught = false;
      cache.cachedRawMessage(OperationalMessage.CHECKHEALTH);
    } catch (Exception e) {
      caught = true;
    }
    assertTrue(caught);
    
    try {
      caught = false;
      cache.cachedRawMessage(OperationalMessage.DIR);
    } catch (Exception e) {
      caught = true;
    }
    assertTrue(caught);
    
    try {
      caught = false;
      cache.cachedRawMessage(OperationalMessage.SPLIT);
    } catch (Exception e) {
      caught = true;
    }
    assertTrue(caught);
  }
}

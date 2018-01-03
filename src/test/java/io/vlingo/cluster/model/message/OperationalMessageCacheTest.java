// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;
import io.vlingo.common.message.RawMessage;

public class OperationalMessageCacheTest {

  @Test
  public void testCachedMessages() {
    final OperationalMessageCache cache = new OperationalMessageCache(Id.of(1), new Name("node1"));
    
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
    final OperationalMessageCache cache = new OperationalMessageCache(Id.of(1), new Name("node1"));
    
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

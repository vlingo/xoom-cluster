// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.testkit.TestActor;
import io.vlingo.xoom.actors.testkit.TestWorld;
import io.vlingo.xoom.cluster.model.AbstractClusterTest;
import io.vlingo.xoom.cluster.model.message.OperationalMessage;
import io.vlingo.xoom.common.pool.ElasticResourcePool;
import io.vlingo.xoom.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.xoom.wire.message.ConsumerByteBufferPool;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public class OperationalOutboundStreamTest extends AbstractClusterTest {
  private MockManagedOutboundChannelProvider channelProvider;
  private Id localNodeId;
  private Node localNode;
  private ConsumerByteBufferPool pool;
  private TestActor<OperationalOutboundStream> outboundStream;
  private TestWorld world;

  @Test
  public void testDirectory() throws Exception {
    outboundStream.actor().directory(new HashSet<Node>(config.allNodes()));

    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
      assertTrue(message.isDirectory());
      assertEquals(localNodeId, message.id());
    }
  }

  @Test
  public void testElect() throws Exception {
    outboundStream.actor().elect(config.allGreaterNodes(localNodeId));

    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
      assertTrue(message.isElect());
      assertEquals(localNodeId, message.id());
    }
  }

  @Test
  public void testJoin() throws Exception {
    outboundStream.actor().join();

    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
      assertTrue(message.isJoin());
      assertEquals(localNodeId, message.id());
    }
}

  @Test
  public void testLeader() throws Exception {
    outboundStream.actor().leader();

    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
      assertTrue(message.isLeader());
      assertEquals(localNodeId, message.id());
    }
  }

  @Test
  public void testLeaderOfId() throws Exception {
    final Id targetId = Id.of(3);

    outboundStream.actor().leader(targetId);

    final ManagedOutboundChannel channel = channelProvider.channelFor(targetId);
    final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
    assertTrue(message.isLeader());
    assertEquals(localNodeId, message.id());
  }

  @Test
  public void testLeave() throws Exception {
    outboundStream.actor().leave();

    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
      assertTrue(message.isLeave());
      assertEquals(localNodeId, message.id());
    }
  }

  @Test
  public void testPing() throws Exception {
    final Id targetId = Id.of(3);

    outboundStream.actor().ping(targetId);

    final ManagedOutboundChannel channel = channelProvider.channelFor(targetId);
    final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
    assertTrue(message.isPing());
    assertEquals(localNodeId, message.id());
  }

  @Test
  public void testPulseToTarget() throws Exception {
    final Id targetId = Id.of(3);

    outboundStream.actor().pulse(targetId);

    final ManagedOutboundChannel channel = channelProvider.channelFor(targetId);
    final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
    assertTrue(message.isPulse());
    assertEquals(localNodeId, message.id());
  }

  @Test
  public void testPulse() throws Exception {
    outboundStream.actor().pulse();

    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
      assertTrue(message.isPulse());
      assertEquals(localNodeId, message.id());
    }
  }

  @Test
  public void testSplit() throws Exception {
    final Id targetNodeId = Id.of(2);
    final Id currentLeaderId = Id.of(3);

    outboundStream.actor().split(targetNodeId, currentLeaderId);

    final ManagedOutboundChannel channel = channelProvider.channelFor(targetNodeId);
    final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
    assertTrue(message.isSplit());
    assertEquals(currentLeaderId, message.id());
  }

  @Test
  public void testVote() throws Exception {
    final Id targetNodeId = Id.of(2);

    outboundStream.actor().vote(targetNodeId);

    final ManagedOutboundChannel channel = channelProvider.channelFor(targetNodeId);
    final OperationalMessage message = OperationalMessage.messageFrom(mock(channel).writes.get(0));
    assertTrue(message.isVote());
    assertEquals(localNodeId, message.id());
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    world = TestWorld.start("test-outbound-stream");

    localNodeId = Id.of(1);

    localNode = config.nodeMatching(localNodeId);

    channelProvider = new MockManagedOutboundChannelProvider(localNodeId, config);

    pool = new ConsumerByteBufferPool(
            ElasticResourcePool.Config.of(10), properties.applicationBufferSize());

    outboundStream =
            world.actorFor(
                    OperationalOutboundStream.class,
                    Definition.has(
                            OperationalOutboundStreamActor.class,
                            Definition.parameters(localNode, channelProvider, pool)));
  }

  @Override
  @After
  public void tearDown() {
    world.terminate();
  }

  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }

  private List<ManagedOutboundChannel> allTargetChannels() {
    return new ArrayList<>(channelProvider.allOtherNodeChannels().values());
  }
}

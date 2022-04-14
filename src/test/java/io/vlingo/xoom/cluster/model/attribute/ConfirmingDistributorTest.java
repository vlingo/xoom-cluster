// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.testkit.TestActor;
import io.vlingo.xoom.actors.testkit.TestUntil;
import io.vlingo.xoom.cluster.model.AbstractClusterTest;
import io.vlingo.xoom.cluster.model.attribute.message.ApplicationMessageType;
import io.vlingo.xoom.cluster.model.message.OperationalMessage;
import io.vlingo.xoom.cluster.model.outbound.MockManagedOutboundChannel;
import io.vlingo.xoom.cluster.model.outbound.MockManagedOutboundChannelProvider;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStreamActor;
import io.vlingo.xoom.common.pool.ElasticResourcePool;
import io.vlingo.xoom.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.xoom.wire.message.ConsumerByteBufferPool;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public class ConfirmingDistributorTest extends AbstractClusterTest {
  private MockManagedOutboundChannelProvider channelProvider;
  private ConfirmingDistributor confirmingDistributor;
  private Id localNodeId;
  private Node localNode;
  private ConsumerByteBufferPool pool;
  private TestActor<OperationalOutboundStream> outboundStream;
  private AttributeSet set;
  private TrackedAttribute tracked;

  @Test
  public void testAcknowledgeConfirmation() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);

    final Collection<String> trackingIds = confirmingDistributor.allTrackingIds();

    assertEquals(1, trackingIds.size());

    final String tackingId = trackingIds.iterator().next();

    assertEquals(2, confirmingDistributor.unconfirmedNodesFor(tackingId).size());
    confirmingDistributor.acknowledgeConfirmation(tackingId, confirmingDistributor.unconfirmedNodesFor(tackingId).iterator().next());
    assertEquals(1, confirmingDistributor.unconfirmedNodesFor(tackingId).size());
    confirmingDistributor.acknowledgeConfirmation(tackingId, confirmingDistributor.unconfirmedNodesFor(tackingId).iterator().next());
    assertEquals(0, confirmingDistributor.unconfirmedNodesFor(tackingId).size());
  }

  @Test
  public void testConfirmCreateAttributeSet() {
    confirmingDistributor.confirmCreate("123", set, localNode);

    singleChannelMessageAssertions();

    assertEquals(1, application.informAttributeSetCreated.get());
  }

  @Test
  public void testConfirmAddAttribute() {
    confirmingDistributor.confirm("123", set, tracked, ApplicationMessageType.AddAttribute, localNode);

    singleChannelMessageAssertions();
  }

  @Test
  public void testConfirmReplaceAttribute() {
    confirmingDistributor.confirm("123", set, tracked, ApplicationMessageType.ReplaceAttribute, localNode);

    singleChannelMessageAssertions();
  }

  @Test
  public void testConfirmRemoveAttribute() {
    confirmingDistributor.confirm("123", set, tracked, ApplicationMessageType.RemoveAttribute, localNode);

    singleChannelMessageAssertions();
  }

  @Test
  public void testConfirmRemoveAttributeSet() {
    confirmingDistributor.confirmRemove("123", set, localNode);

    singleChannelMessageAssertions();

    assertEquals(1, application.informAttributeSetRemoved.get());
  }

  @Test
  public void testDistributeCreateAttributeSet() {
    confirmingDistributor.distributeCreate(set);

    multiChannelMessageAssertions(2);

    assertEquals(1, application.informAttributeSetCreated.get());
    assertEquals(1, application.informAttributeAdded.get());
  }

  @Test
  public void testDistributeAddAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);

    multiChannelMessageAssertions(1);

    assertEquals(1, application.informAttributeAdded.get());
  }

  @Test
  public void testDistributeReplaceAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.ReplaceAttribute);

    multiChannelMessageAssertions(1);

    assertEquals(1, application.informAttributeReplaced.get());
  }

  @Test
  public void testDistributeRemoveAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.RemoveAttribute);

    multiChannelMessageAssertions(1);

    assertEquals(1, application.informAttributeRemoved.get());
  }

  @Test
  public void testDistributeRemoveAttributeSet() {
    confirmingDistributor.distributeRemove(set);

    multiChannelMessageAssertions(2);

    assertEquals(1, application.informAttributeSetRemoved.get());
    assertEquals(1, application.informAttributeRemoved.get());
  }

  @Test
  public void testRedistributeUnconfirmed() {
    final Iterator<Node> iter = config.allOtherNodes(localNodeId).iterator();

    final ManagedOutboundChannel channel2 = channelProvider.channelFor(iter.next().id());
    mock(channel2).until = TestUntil.happenings(1);

    final ManagedOutboundChannel channel3 = channelProvider.channelFor(iter.next().id());
    mock(channel3).until = TestUntil.happenings(1);

    final AttributeSet set = AttributeSet.named("test-set");
    final TrackedAttribute tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));

    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);

    confirmingDistributor.redistributeUnconfirmed();

    mock(channel2).until.completes();
    assertEquals(1, mock(channel2).writes.size());

    mock(channel3).until.completes();
    assertEquals(1, mock(channel3).writes.size());
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    localNodeId = Id.of(1);

    localNode = config.nodeMatching(localNodeId);

    set = AttributeSet.named("test-set");

    tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));

    channelProvider = new MockManagedOutboundChannelProvider(localNodeId, config);

    pool = new ConsumerByteBufferPool(ElasticResourcePool.Config.of(10), properties.operationalBufferSize());

    outboundStream =
            testWorld.actorFor(
                    OperationalOutboundStream.class,
                    Definition.has(
                            OperationalOutboundStreamActor.class,
                            Definition.parameters(localNode, channelProvider, pool)));

    confirmingDistributor = new ConfirmingDistributor(application, localNode, outboundStream.actor(), config);
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
  }

  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }

  private void multiChannelMessageAssertions(final int messageCount) {
    final Iterator<Node> iter = config.allOtherNodes(localNodeId).iterator();
    final ManagedOutboundChannel channel2 = channelProvider.channelFor(iter.next().id());
    final ManagedOutboundChannel channel3 = channelProvider.channelFor(iter.next().id());
    assertEquals(messageCount, mock(channel2).writes.size());
    assertEquals(messageCount, mock(channel3).writes.size());
    final OperationalMessage message2 = OperationalMessage.messageFrom(mock(channel2).writes.get(0));
    final OperationalMessage message3 = OperationalMessage.messageFrom(mock(channel3).writes.get(0));
    assertTrue(message2.isApp());
    assertTrue(message3.isApp());
    assertEquals(localNodeId, message2.id());
    assertEquals(localNodeId, message3.id());
    assertEquals(message2, message3);
  }

  private void singleChannelMessageAssertions() {
    final ManagedOutboundChannel channel1 = channelProvider.channelFor(localNodeId);
    final OperationalMessage message1 = OperationalMessage.messageFrom(mock(channel1).writes.get(0));

    assertEquals(1, mock(channel1).writes.size());
    assertEquals(localNodeId, message1.id());
  }
}

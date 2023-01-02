// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.testkit.TestActor;
import io.vlingo.xoom.cluster.model.AbstractClusterTest;
import io.vlingo.xoom.cluster.model.attribute.message.ApplicationMessageType;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.cluster.model.outbound.MockManagedOutboundChannel;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStreamActor;
import io.vlingo.xoom.common.pool.ElasticResourcePool;
import io.vlingo.xoom.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.xoom.wire.message.ConsumerByteBufferPool;
import io.vlingo.xoom.wire.node.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ConfirmingDistributorTest extends AbstractClusterTest {
  private ConfirmingDistributor confirmingDistributor;
  private Node localNode;
  private MockCluster mockCluster;
  private ConsumerByteBufferPool pool;
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

    assertEquals(1, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeSetCreated.get());
  }

  @Test
  public void testConfirmAddAttribute() {
    confirmingDistributor.confirm("123", set, tracked, ApplicationMessageType.AddAttribute, localNode);

    assertEquals(1, mockCluster.messagesTo(localNode));
  }

  @Test
  public void testConfirmReplaceAttribute() {
    confirmingDistributor.confirm("123", set, tracked, ApplicationMessageType.ReplaceAttribute, localNode);

    assertEquals(1, mockCluster.messagesTo(localNode));
  }

  @Test
  public void testConfirmRemoveAttribute() {
    confirmingDistributor.confirm("123", set, tracked, ApplicationMessageType.RemoveAttribute, localNode);

    assertEquals(1, mockCluster.messagesTo(localNode));
  }

  @Test
  public void testConfirmRemoveAttributeSet() {
    confirmingDistributor.confirmRemove("123", set, localNode);

    assertEquals(1, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeSetRemoved.get());
  }

  @Test
  public void testDistributeCreateAttributeSet() {
    confirmingDistributor.distributeCreate(set);

    for (Node otherNode : allOtherNodes()) {
      assertEquals(2, mockCluster.messagesTo(otherNode));
    }

    assertEquals(1, application.informAttributeSetCreated.get());
    assertEquals(1, application.informAttributeAdded.get());
  }

  @Test
  public void testDistributeAddAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);

    for (Node otherNode : allOtherNodes()) {
      assertEquals(1, mockCluster.messagesTo(otherNode));
    }

    assertEquals(1, application.informAttributeAdded.get());
  }

  @Test
  public void testDistributeReplaceAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.ReplaceAttribute);

    for (Node otherNode : allOtherNodes()) {
      assertEquals(1, mockCluster.messagesTo(otherNode));
    }

    assertEquals(1, application.informAttributeReplaced.get());
  }

  @Test
  public void testDistributeRemoveAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.RemoveAttribute);

    for (Node otherNode : allOtherNodes()) {
      assertEquals(1, mockCluster.messagesTo(otherNode));
    }

    assertEquals(1, application.informAttributeRemoved.get());
  }

  @Test
  public void testDistributeRemoveAttributeSet() {
    confirmingDistributor.distributeRemove(set);

    for (Node otherNode : allOtherNodes()) {
      assertEquals(2, mockCluster.messagesTo(otherNode));
    }

    assertEquals(1, application.informAttributeSetRemoved.get());
    assertEquals(1, application.informAttributeRemoved.get());
  }

  @Test
  public void testRedistributeUnconfirmed() {
    final AttributeSet set = AttributeSet.named("test-set");
    final TrackedAttribute tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));

    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);

    confirmingDistributor.redistributeUnconfirmed();

    for (Node otherNode : allOtherNodes()) {
      assertEquals(1, mockCluster.messagesTo(otherNode));
    }
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    localNode = config.localNode();

    set = AttributeSet.named("test-set");

    tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));

    mockCluster = new MockCluster(localNode, allNodes, allOtherNodes());
    Registry mockRegistry = mockCluster.mockHealthyRegistry();

    pool = new ConsumerByteBufferPool(ElasticResourcePool.Config.of(10), properties.operationalBufferSize());

    TestActor<OperationalOutboundStream> outboundStream = testWorld.actorFor(
            OperationalOutboundStream.class,
            Definition.has(
                    OperationalOutboundStreamActor.class,
                    Definition.parameters(mockCluster.cluster, mockRegistry, pool)));

    confirmingDistributor = new ConfirmingDistributor(application, localNode, outboundStream.actor(), mockRegistry::allOtherNodes, testWorld.defaultLogger());
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
  }

  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }
}

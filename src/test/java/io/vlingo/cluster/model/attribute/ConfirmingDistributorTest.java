// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.Properties;
import io.vlingo.cluster.model.attribute.message.ApplicationMessageType;
import io.vlingo.cluster.model.message.OperationalMessage;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.cluster.model.outbound.ManagedOutboundChannel;
import io.vlingo.cluster.model.outbound.MockManagedOutboundChannel;
import io.vlingo.cluster.model.outbound.MockManagedOutboundChannelProvider;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.cluster.model.outbound.OperationalOutboundStreamActor;
import io.vlingo.common.message.ByteBufferPool;

public class ConfirmingDistributorTest extends AbstractClusterTest {
  private MockManagedOutboundChannelProvider channelProvider;
  private ConfirmingDistributor confirmingDistributor;
  private Id localNodeId;
  private Node localNode;
  private ByteBufferPool pool;
  private TestActor<OperationalOutboundStream> outboundStream;
  private AttributeSet set;
  private TrackedAttribute tracked;

  @Test
  public void testAcknowledgeConfirmation() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);
    
    final Collection<String> trackingIds = confirmingDistributor.allTrackingIds();
    
    assertEquals(1, trackingIds.size());
    
    final String tackingId = trackingIds.iterator().next();
    
    final Collection<Node> nodes = confirmingDistributor.unconfirmedNodesFor(tackingId);
    
    assertEquals(2, nodes.size());
    
    final List<Node> list = new ArrayList<>(nodes);
    
    confirmingDistributor.acknowledgeConfirmation(tackingId, list.get(0));
    assertEquals(1, nodes.size());
    confirmingDistributor.acknowledgeConfirmation(tackingId, list.get(1));
    assertEquals(0, nodes.size());
  }

  @Test
  public void testConfirmAttributeSet() {
    confirmingDistributor.confirm("123", set, localNode);
    
    singleChannelMessageAssertions();
    
    assertEquals(1, application.informAttributeSetCreated);
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
  public void testDistributeAttributeSet() {
    confirmingDistributor.distribute(set);
    
    multiChannelMessageAssertions(2);
    
    assertEquals(1, application.informAttributeSetCreated);
    assertEquals(1, application.informAttributeAdded);
  }

  @Test
  public void testDistributeAddAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);
    
    multiChannelMessageAssertions(1);
    
    assertEquals(1, application.informAttributeAdded);
  }

  @Test
  public void testDistributeReplaceAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.ReplaceAttribute);
    
    multiChannelMessageAssertions(1);
    
    assertEquals(1, application.informAttributeReplaced);
  }

  @Test
  public void testDistributeRemoveAttribute() {
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.RemoveAttribute);
    
    multiChannelMessageAssertions(1);
    
    assertEquals(1, application.informAttributeRemoved);
  }
  
  @Test
  public void testRedistributeUnconfirmed() {
    final AttributeSet set = AttributeSet.named("test-set");
    final TrackedAttribute tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));
    
    confirmingDistributor.distribute(set, tracked, ApplicationMessageType.AddAttribute);
    
    this.delay = 100L + Properties.instance.clusterAttributesRedistributionInterval();
    pause();
    
    confirmingDistributor.redistributeUnconfirmed();
    
    final Iterator<Node> iter = config.allOtherConfiguredNodes(localNodeId).iterator();
    final ManagedOutboundChannel channel2 = channelProvider.channelFor(iter.next().id());
    final ManagedOutboundChannel channel3 = channelProvider.channelFor(iter.next().id());
    
    assertEquals(2, mock(channel2).writes.size());
    assertEquals(2, mock(channel3).writes.size());
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    localNodeId = Id.of(1);
    
    localNode = config.configuredNodeMatching(localNodeId);
    
    set = AttributeSet.named("test-set");
    
    tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));
    
    channelProvider = new MockManagedOutboundChannelProvider(localNodeId, config);
    
    pool = new ByteBufferPool(10, properties.operationalBufferSize());
    
    outboundStream =
            testWorld.actorFor(
                    Definition.has(
                            OperationalOutboundStreamActor.class,
                            Definition.parameters(localNode, channelProvider, pool)),
                    OperationalOutboundStream.class);
    
    confirmingDistributor = new ConfirmingDistributor(application, localNode, outboundStream.actor(), config);
  }
  
  @After
  public void tearDown() {
    super.tearDown();
  }

  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }

  private void multiChannelMessageAssertions(final int messageCount) {
    final Iterator<Node> iter = config.allOtherConfiguredNodes(localNodeId).iterator();
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

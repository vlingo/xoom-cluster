// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.attribute.message.AddAttribute;
import io.vlingo.cluster.model.attribute.message.ApplicationMessage;
import io.vlingo.cluster.model.attribute.message.ApplicationMessageType;
import io.vlingo.cluster.model.attribute.message.ConfirmAttribute;
import io.vlingo.cluster.model.attribute.message.ConfirmAttributeSet;
import io.vlingo.cluster.model.attribute.message.CreateAttributeSet;
import io.vlingo.cluster.model.attribute.message.RemoveAttribute;
import io.vlingo.cluster.model.attribute.message.ReplaceAttribute;
import io.vlingo.cluster.model.message.ApplicationSays;
import io.vlingo.cluster.model.message.MessageConverters;
import io.vlingo.cluster.model.outbound.MockManagedOutboundChannel;
import io.vlingo.cluster.model.outbound.MockManagedOutboundChannelProvider;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.cluster.model.outbound.OperationalOutboundStreamActor;
import io.vlingo.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.wire.message.ByteBufferPool;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class AttributesAgentActorTest extends AbstractClusterTest {
  private MockManagedOutboundChannelProvider channelProvider;
  private Id localNodeId;
  private Node localNode;
  private MockConfirmationInterest interest;
  private ByteBufferPool pool;
  private TestActor<OperationalOutboundStream> outboundStream;
  private AttributeSet set;
  private TrackedAttribute tracked;

  @Test
  public void testAdd() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    AttributesAgent.class);
    
    agent.actor().add("test=set", "test-attr", "test-value");
    
    final Iterator<Node> iter = config.allOtherNodes(localNodeId).iterator();
    final ManagedOutboundChannel channel2 = channelProvider.channelFor(iter.next().id());
    final ManagedOutboundChannel channel3 = channelProvider.channelFor(iter.next().id());
    assertEquals(2, mock(channel2).writes.size());
    assertEquals(2, mock(channel3).writes.size());
    
    assertEquals("test-value", AttributesClient.instance().attribute("test=set", "test-attr").value);
  }

  @Test
  public void testReplace() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    AttributesAgent.class);
    
    agent.actor().add("test=set", "test-attr", "test-value1");
    agent.actor().replace("test=set", "test-attr", "test-value2");
    
    final Iterator<Node> iter = config.allOtherNodes(localNodeId).iterator();
    final ManagedOutboundChannel channel2 = channelProvider.channelFor(iter.next().id());
    final ManagedOutboundChannel channel3 = channelProvider.channelFor(iter.next().id());
    assertEquals(3, mock(channel2).writes.size());
    assertEquals(3, mock(channel3).writes.size());
    
    assertEquals("test-value2", AttributesClient.instance().attribute("test=set", "test-attr").value);
  }

  @Test
  public void testRemove() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    AttributesAgent.class);
    
    agent.actor().add("test=set", "test-attr", "test-value1");
    agent.actor().remove("test=set", "test-attr");
    
    final Iterator<Node> iter = config.allOtherNodes(localNodeId).iterator();
    final ManagedOutboundChannel channel2 = channelProvider.channelFor(iter.next().id());
    final ManagedOutboundChannel channel3 = channelProvider.channelFor(iter.next().id());
    assertEquals(3, mock(channel2).writes.size());
    assertEquals(3, mock(channel3).writes.size());
    
    assertEquals(Attribute.Undefined, AttributesClient.instance().attribute("test=set", "test-attr"));
  }

  @Test
  public void testInboundStreamInterestCreateAttributeSet() throws Exception {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);

    final ApplicationMessage message = CreateAttributeSet.from(localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), message), null);
    
    final ManagedOutboundChannel channel1 = channelProvider.channelFor(localNodeId);
    assertEquals(1, mock(channel1).writes.size());
    assertEquals(1, application.informAttributeSetCreated);
  }

  @Test
  public void testInboundStreamInterestAddAttribute() throws Exception {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);

    final ApplicationMessage message = AddAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), message), null);
    
    final ManagedOutboundChannel channel1 = channelProvider.channelFor(localNodeId);
    assertEquals(1, mock(channel1).writes.size());
    assertEquals(1, application.informAttributeAdded);
  }

  @Test
  public void testInboundStreamInterestReplaceAttribute() throws Exception {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);

    final ApplicationMessage addMessage = AddAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), addMessage), null);
    final ApplicationMessage replaceMessage = ReplaceAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), replaceMessage), null);
    
    final ManagedOutboundChannel channel1 = channelProvider.channelFor(localNodeId);
    assertEquals(2, mock(channel1).writes.size());
    assertEquals(1, application.informAttributeAdded);
    assertEquals(1, application.informAttributeReplaced);
  }

  @Test
  public void testInboundStreamInterestRemoveAttribute() throws Exception {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);

    final ApplicationMessage addMessage = AddAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), addMessage), null);
    final ApplicationMessage removeMessage = RemoveAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), removeMessage), null);
    
    final ManagedOutboundChannel channel1 = channelProvider.channelFor(localNodeId);
    assertEquals(2, mock(channel1).writes.size());
    assertEquals(1, application.informAttributeAdded);
    assertEquals(1, application.informAttributeRemoved);
  }

  @Test
  public void testConfirmAttributeSet() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);
    
    final ApplicationMessage confirm = new ConfirmAttributeSet("123", localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), confirm), null);
    assertEquals(1, interest.confirmed);
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(confirm.type, interest.type);
  }

  @Test
  public void testConfirmAddAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);
    
    final ApplicationMessage confirm = new ConfirmAttribute("123", localNode, set, tracked, ApplicationMessageType.ConfirmAddAttribute);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), confirm), null);
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(tracked.attribute.name, interest.attributeName);
    assertEquals(confirm.type, interest.type);
    assertEquals(1, interest.confirmed);
  }

  @Test
  public void testConfirmReplaceAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);
    
    final ApplicationMessage confirm = new ConfirmAttribute("123", localNode, set, tracked, ApplicationMessageType.ConfirmReplaceAttribute);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), confirm), null);
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(tracked.attribute.name, interest.attributeName);
    assertEquals(confirm.type, interest.type);
    assertEquals(1, interest.confirmed);
  }

  @Test
  public void testConfirmRemoveAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), config, interest)),
                    InboundStreamInterest.class);
    
    final ApplicationMessage confirm = new ConfirmAttribute("123", localNode, set, tracked, ApplicationMessageType.ConfirmRemoveAttribute);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(localNodeId, localNode.name(), confirm), null);
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(tracked.attribute.name, interest.attributeName);
    assertEquals(confirm.type, interest.type);
    assertEquals(1, interest.confirmed);
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    localNodeId = Id.of(1);
    
    localNode = config.nodeMatching(localNodeId);
    
    set = AttributeSet.named("test-set");
    
    tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));
    
    channelProvider = new MockManagedOutboundChannelProvider(localNodeId, config);
    
    pool = new ByteBufferPool(10, properties.operationalBufferSize());
    
    interest = new MockConfirmationInterest();
    
    outboundStream =
            testWorld.actorFor(
                    Definition.has(
                            OperationalOutboundStreamActor.class,
                            Definition.parameters(localNode, channelProvider, pool)),
                    OperationalOutboundStream.class);
  }

  private RawMessage rawMessageFor(final Id id, final Name name, final ApplicationMessage message) {
    final ByteBuffer messageBuffer = ByteBuffer.allocate(4096);
    final ApplicationSays says = ApplicationSays.from(id, name, message.toPayload());
    MessageConverters.messageToBytes(says, messageBuffer);
    return Converters.toRawMessage(Id.of(1).value(), messageBuffer);
  }

  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }
}

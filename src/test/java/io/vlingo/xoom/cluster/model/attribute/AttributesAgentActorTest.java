// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.testkit.TestActor;
import io.vlingo.xoom.cluster.model.AbstractClusterTest;
import io.vlingo.xoom.cluster.model.attribute.message.*;
import io.vlingo.xoom.cluster.model.message.ApplicationSays;
import io.vlingo.xoom.cluster.model.message.MessageConverters;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.cluster.model.outbound.MockManagedOutboundChannel;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStreamActor;
import io.vlingo.xoom.common.pool.ElasticResourcePool;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.xoom.wire.message.ByteBufferAllocator;
import io.vlingo.xoom.wire.message.ConsumerByteBufferPool;
import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class AttributesAgentActorTest extends AbstractClusterTest {
  private MockConfirmationInterest interest;
  private ConsumerByteBufferPool pool;
  private MockCluster mockCluster;
  private TestActor<OperationalOutboundStream> outboundStream;
  private AttributeSet set;
  private TrackedAttribute tracked;

  @Test
  public void testAdd() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    AttributesAgent.class,
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    agent.actor().add("test-set", "test-attr", "test-value");

    for (Node otherNode : allOtherNodes()) {
      assertEquals(2, mockCluster.messagesTo(otherNode));
    }

    assertEquals("test-value", application.attributesClient.attribute("test-set", "test-attr").value);
  }

  @Test
  public void testReplace() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    AttributesAgent.class,
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    agent.actor().add("test-set", "test-attr", "test-value1");
    agent.actor().replace("test-set", "test-attr", "test-value2");

    for (Node otherNode : allOtherNodes()) {
      assertEquals(3, mockCluster.messagesTo(otherNode));
    }

    assertEquals("test-value2", application.attributesClient.attribute("test-set", "test-attr").value);
  }

  @Test
  public void testRemove() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    AttributesAgent.class,
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    agent.actor().add("test-set", "test-attr", "test-value1");
    agent.actor().remove("test-set", "test-attr");

    for (Node otherNode : allOtherNodes()) {
      assertEquals(3, mockCluster.messagesTo(otherNode));
    }

    assertEquals(Attribute.Undefined, application.attributesClient.attribute("test-set", "test-attr"));
  }

  @Test
  public void testRemoveAttributeSet() {
    TestActor<AttributesAgent> agent =
            testWorld.actorFor(
                    AttributesAgent.class,
                    Definition.has(
                            AttributesAgentActor.class,
                            Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    agent.actor().add("test-set", "test-attr1", "test-value1");
    agent.actor().add("test-set", "test-attr2", "test-value2");
    agent.actor().add("test-set", "test-attr3", "test-value3");
    agent.actor().removeAll("test-set");

    // 1. create set, 2. add attr, 3. add attr, 4. add attr, 5. remove attr, 6. remove attr, 7. remove attr, 8. remove set
    for (Node otherNode : allOtherNodes()) {
      assertEquals(8, mockCluster.messagesTo(otherNode));
    }

    assertEquals(Attribute.Undefined, application.attributesClient.attribute("test-set", "test-attr1"));
    assertEquals(Attribute.Undefined, application.attributesClient.attribute("test-set", "test-attr2"));
    assertEquals(Attribute.Undefined, application.attributesClient.attribute("test-set", "test-attr3"));
    assertEquals(0, application.attributesClient.allOf("test-set").size());
    assertEquals(0, application.attributesClient.all().size());
  }

  @Test
  public void testInboundStreamInterestCreateAttributeSet() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage message = CreateAttributeSet.from(localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), message));

    assertEquals(1, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeSetCreated.get());
  }

  @Test
  public void testInboundStreamInterestAddAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage message = AddAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), message));

    assertEquals(1, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeAdded.get());
  }

  @Test
  public void testInboundStreamInterestReplaceAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage addMessage = AddAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), addMessage));
    final ApplicationMessage replaceMessage = ReplaceAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), replaceMessage));

    assertEquals(2, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeAdded.get());
    assertEquals(1, application.informAttributeReplaced.get());
  }

  @Test
  public void testInboundStreamInterestRemoveAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage addMessage = AddAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), addMessage));
    final ApplicationMessage removeMessage = RemoveAttribute.from(localNode, set, tracked);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), removeMessage));

    assertEquals(2, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeAdded.get());
    assertEquals(1, application.informAttributeRemoved.get());
  }

  @Test
  public void testInboundStreamInterestRemoveAttributeSet() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage createMessage = CreateAttributeSet.from(localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), createMessage));
    final ApplicationMessage removeMessage = RemoveAttributeSet.from(localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), removeMessage));

    assertEquals(2, mockCluster.messagesTo(localNode));
    assertEquals(1, application.informAttributeSetCreated.get());
    assertEquals(1, application.informAttributeSetRemoved.get());
  }

  @Test
  public void testConfirmCreateAttributeSet() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage confirm = new ConfirmCreateAttributeSet("123", localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), confirm));
    assertEquals(1, interest.confirmed);
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(confirm.type, interest.type);
  }

  @Test
  public void testConfirmAddAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage confirm = new ConfirmAttribute("123", localNode, set, tracked, ApplicationMessageType.ConfirmAddAttribute);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), confirm));
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(tracked.attribute.name, interest.attributeName);
    assertEquals(confirm.type, interest.type);
    assertEquals(1, interest.confirmed);
  }

  @Test
  public void testConfirmReplaceAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage confirm = new ConfirmAttribute("123", localNode, set, tracked, ApplicationMessageType.ConfirmReplaceAttribute);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), confirm));
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(tracked.attribute.name, interest.attributeName);
    assertEquals(confirm.type, interest.type);
    assertEquals(1, interest.confirmed);
  }

  @Test
  public void testConfirmRemoveAttribute() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage confirm = new ConfirmAttribute("123", localNode, set, tracked, ApplicationMessageType.ConfirmRemoveAttribute);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), confirm));
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(tracked.attribute.name, interest.attributeName);
    assertEquals(confirm.type, interest.type);
    assertEquals(1, interest.confirmed);
  }

  @Test
  public void testConfirmRemoveAttributeSet() {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(AttributesAgentActor.class, Definition.parameters(localNode, application, outboundStream.actor(), interest, mockCluster.mockHealthyRegistry(), testWorld.defaultLogger())));

    final ApplicationMessage confirm = new ConfirmRemoveAttributeSet("123", localNode, set);
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, rawMessageFor(config.localNode().id(), localNode.name(), confirm));
    assertEquals(1, interest.confirmed);
    assertEquals(set.name, interest.attributeSetName);
    assertEquals(confirm.type, interest.type);
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    localNode = config.localNode();

    set = AttributeSet.named("test-set");

    tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));

    pool = new ConsumerByteBufferPool(
            ElasticResourcePool.Config.of(10), properties.operationalBufferSize());

    interest = new MockConfirmationInterest();

    mockCluster = new MockCluster(localNode, allNodes, allOtherNodes());
    Registry mockRegistry = mockCluster.mockHealthyRegistry();

    outboundStream =
            testWorld.actorFor(
                    OperationalOutboundStream.class,
                    Definition.has(
                            OperationalOutboundStreamActor.class,
                            Definition.parameters(mockCluster.cluster, mockRegistry, pool)));
  }

  private RawMessage rawMessageFor(final Id id, final Name name, final ApplicationMessage message) {
    final ByteBuffer messageBuffer = ByteBufferAllocator.allocate(4096);
    final ApplicationSays says = ApplicationSays.from(id, name, message.toPayload());
    MessageConverters.messageToBytes(says, messageBuffer);
    return Converters.toRawMessage(Id.of(1).value(), messageBuffer);
  }

  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }
}

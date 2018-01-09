// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.node.AddressType;

public class InboundStreamTest extends AbstractClusterTest {
  private TestActor<InboundStream> inboundStream;
  private MockInboundStreamInterest interest;
  private MockInboundReader reader;
  private TestWorld world;
  
  @Test
  public void testInbound() throws Exception {
    inboundStream.actor().start();
    pause();
    assertTrue(interest.messageCount > 0);
    
    int count = 0;
    for (final String message : interest.messages) {
      ++count;
      assertEquals(MockInboundReader.MessagePrefix + count, message);
    }
    
    assertEquals(count, reader.probeChannelCount);
    assertEquals(count, reader.inboundClientChannelWriteCount);
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    delay = 1000L;
    
    world = TestWorld.start("test-inbound-stream");
    
    interest = new MockInboundStreamInterest();
    
    reader = new MockInboundReader();
    
    final Definition definition =
            Definition.has(
                    InboundStreamActor.class,
                    Definition.parameters(interest, AddressType.OP, reader),
                    "cluster-test-inbound");
    
    inboundStream = world.actorFor(definition, InboundStream.class);
  }
  
  @After
  public void tearDown() {
    world.terminate();
  }
}

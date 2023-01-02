// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.vlingo.xoom.cluster.model.AbstractClusterTest;
import io.vlingo.xoom.wire.node.Address;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Host;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;

public class LocalRegistryTest extends AbstractClusterTest {

  @Test
  public void testNoLiveNodes() {
    final LocalRegistry registry = new LocalRegistry(testWorld.defaultLogger(), allNodes.get(0), 2);
    assertTrue(registry.nodes().isEmpty());
  }
  
  @Test
  public void testJoin() {
    final LocalRegistry registry = join3Nodes();
    
    registry.join(nodeOf(4));
    
    assertEquals(4, registry.nodes().size());
    
    assertTrue(registry.containsNode(idOf(1)));
    assertTrue(registry.containsNode(idOf(2)));
    assertTrue(registry.containsNode(idOf(3)));
    assertTrue(registry.containsNode(idOf(4)));
  }

  @Test
  public void testLeave() {
    final LocalRegistry registry = join3Nodes();
    
    assertTrue(registry.containsNode(idOf(1)));
    assertTrue(registry.containsNode(idOf(2)));
    assertTrue(registry.containsNode(idOf(3)));
    
    registry.leave(idOf(2));
    
    assertTrue(registry.containsNode(idOf(1)));
    assertFalse(registry.containsNode(idOf(2)));
    assertTrue(registry.containsNode(idOf(3)));
  }
  
  private Id idOf(final int idValue) {
    return Id.of(idValue);
  }
  
  // Note: join() is tested by nearly every test
  
  private LocalRegistry join3Nodes() {
    final LocalRegistry registry = new LocalRegistry(testWorld.defaultLogger(), allNodes.get(0), 2);
    final Node node1 = nodeOf(1);
    final Node node2 = nodeOf(2);
    final Node node3 = nodeOf(3);

    registry.join(node1);
    registry.join(node2);
    registry.join(node3);
    
    return registry;
  }
  
  private Node nodeOf(final int idValue) {
    final Id id = Id.of(idValue);
    final Name name = new Name("node" + idValue);
    final Address opAddress = new Address(Host.of("localhost"), 1111 + idValue, AddressType.OP);
    final Address appAddress = new Address(Host.of("localhost"), 1111 + idValue + 1, AddressType.APP);
    final Node node = new Node(id, name, false, opAddress, appAddress);
    
    return node;
  }
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeTest {

  @Test
  public void testNodeCreationState() {
    final Id id1 = Id.of(1);
    final Name name1 = new Name("name1");
    final Address opAddress1 = new Address("localhost", 11111, AddressType.OP);
    final Address appAddress1 = new Address("localhost", 11112, AddressType.APP);
    final Node node1 = new Node(id1, name1, opAddress1, appAddress1);
    
    assertFalse(node1.hasMissingPart());
    assertTrue(node1.isValid());

    final Id id2 = Id.of(2);
    final Name name2 = new Name("name2");
    final Address opAddress2 = new Address("localhost", 11113, AddressType.OP);
    final Address appAddress2 = new Address("localhost", 11114, AddressType.APP);
    final Node node2 = new Node(id2, name2, opAddress2, appAddress2);
    
    assertFalse(node2.hasMissingPart());
    assertTrue(node2.isValid());
    
    assertTrue(node2.isLeaderOver(node1.id()));
    
    assertFalse(node1.greaterThan(node2));
    
    assertEquals(-1, node1.compareTo(node2));
    assertEquals(1, node2.compareTo(node1));
  }
}

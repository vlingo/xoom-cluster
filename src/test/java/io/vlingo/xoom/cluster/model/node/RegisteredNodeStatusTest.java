// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
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

import io.vlingo.xoom.wire.node.Address;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Host;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;

public class RegisteredNodeStatusTest {

  @Test
  public void testStatusCreationState() {
    final Id id1 = Id.of(1);
    final Name name1 = new Name("name1");
    final Address opAddress1 = new Address(Host.of("localhost"), 11111, AddressType.OP);
    final Address appAddress1 = new Address(Host.of("localhost"), 11112, AddressType.APP);
    final Node node1 = new Node(id1, name1, opAddress1, appAddress1);

    final RegisteredNodeStatus status = new RegisteredNodeStatus(node1, true, true);
    
    assertTrue(status.isLeader());
    assertTrue(status.isConfirmedByLeader());
    status.confirmedByLeader(false);
    assertFalse(status.isConfirmedByLeader());
    assertEquals(node1, status.node());
    assertTrue(status.lastHealthIndication() > 0L);
  }

  @Test
  public void testStatusTimeout() {
    final Id id1 = Id.of(1);
    final Name name1 = new Name("name1");
    final Address opAddress1 = new Address(Host.of("localhost"), 11111, AddressType.OP);
    final Address appAddress1 = new Address(Host.of("localhost"), 11112, AddressType.APP);
    final Node node1 = new Node(id1, name1, opAddress1, appAddress1);

    final RegisteredNodeStatus status = new RegisteredNodeStatus(node1, true, true);
    
    assertFalse(status.isTimedOut(System.currentTimeMillis(), 100L));
    assertTrue(status.isTimedOut(System.currentTimeMillis() + 4001L, 4000L));
  }
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.attribute.Confirmables.Confirmable;
import io.vlingo.cluster.model.attribute.message.AddAttribute;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

public class ConfirmablesTest extends AbstractClusterTest {
  private Confirmables consumables;
  private Node localNode;
  private Id localNodeId;
  private Node remoteNode2;
  private Node remoteNode3;
  private AttributeSet set;
  private TrackedAttribute tracked;

  @Test
  public void testCreateConfirmables() {
    assertTrue(consumables.allRedistributable().isEmpty());
    
    assertTrue(consumables.allTrackingIds().isEmpty());
    
    assertEquals(Confirmable.NoConfirmable, consumables.confirmableOf("123"));
  }

  @Test
  public void testConfirmConfirmables() {
    final AddAttribute addAttribute = AddAttribute.from(localNode, set, tracked);
    
    consumables.unconfirmed(addAttribute);
    
    assertEquals(1, consumables.allTrackingIds().size());
    assertEquals(2, consumables.confirmableOf(addAttribute.trackingId).unconfirmedNodes().size());
    
    consumables.confirm(addAttribute.trackingId, remoteNode2);
    
    assertEquals(1, consumables.allTrackingIds().size());
    assertEquals(1, consumables.confirmableOf(addAttribute.trackingId).unconfirmedNodes().size());
    
    consumables.confirm(addAttribute.trackingId, remoteNode3);
    
    assertTrue(consumables.allTrackingIds().isEmpty());
  }
  
  @Test
  public void testIsRedistributableAsOf() {
    final AddAttribute addAttribute = AddAttribute.from(localNode, set, tracked);
    
    consumables.unconfirmed(addAttribute);
    
    assertFalse(consumables.confirmableOf(addAttribute.trackingId).isRedistributableAsOf());
    assertEquals(0, consumables.allRedistributable().size());
    
    while (consumables.allRedistributable().size() != 1);
    assertEquals(1, consumables.allRedistributable().size());
  }

  @Test
  public void testUnconfirmedConfirmables() {
    final AddAttribute addAttribute = AddAttribute.from(localNode, set, tracked);
    
    consumables.unconfirmed(addAttribute);
    
    assertFalse(consumables.allTrackingIds().isEmpty());
    
    assertNotNull(consumables.confirmableOf(addAttribute.trackingId));
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    localNodeId = Id.of(1);
    
    localNode = config.nodeMatching(localNodeId);
    
    remoteNode2 = config.nodeMatching(Id.of(2));
    
    remoteNode3 = config.nodeMatching(Id.of(3));

    set = AttributeSet.named("test-set");

    tracked = set.addIfAbsent(Attribute.from("test-attr", "test-value"));

    consumables = new Confirmables(localNode, config.allOtherNodes(localNodeId));
  }
}

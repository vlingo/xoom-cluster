// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TrackedAttributeTest {
  private final AttributeSet set = AttributeSet.named("test");
  
  @Test
  public void testTrackedAttributeOf() {
    final Attribute<Integer> attr1 = Attribute.from("attr1", 1);
    
    final TrackedAttribute tracked1 = TrackedAttribute.of(set, attr1);
    
    assertEquals(attr1, tracked1.attribute);

    final Attribute<Integer> attr2 = Attribute.from("attr1", 1);
    
    final TrackedAttribute tracked2 = TrackedAttribute.of(set, attr2);
    
    assertEquals(attr1, attr2);
    
    assertEquals(attr2, tracked2.attribute);
    
    assertEquals(tracked1, tracked2);
  }
  
  @Test
  public void testAsDistributed() {
    final Attribute<Integer> attr1 = Attribute.from("attr1", 1);
    
    final TrackedAttribute tracked1 = TrackedAttribute.of(set, attr1);
    
    assertFalse(tracked1.distributed);
    
    final TrackedAttribute tracked2 = tracked1.asDistributed();
    
    assertTrue(tracked2.distributed);
    
    assertEquals(tracked1.attribute, tracked2.attribute);
    assertEquals(tracked1.id, tracked2.id);
  }

  @Test
  public void testAbsentPresent() {
    final Attribute<Integer> attr1 = Attribute.from("attr1", 1);
    
    final TrackedAttribute tracked1 = TrackedAttribute.of(set, attr1);
    
    assertFalse(tracked1.isAbsent());
    assertTrue(tracked1.isPresent());
    
    assertTrue(TrackedAttribute.Absent.isAbsent());
    assertFalse(TrackedAttribute.Absent.isPresent());
  }
  
  @Test
  public void testWithAttribute() {
    final Attribute<Integer> attr1 = Attribute.from("attr1", 1);
    
    final TrackedAttribute tracked1 = TrackedAttribute.of(set, attr1);
    
    final Attribute<Integer> attr1ValueModified = Attribute.from("attr1", 2);
    
    final TrackedAttribute tracked2 = tracked1.withAttribute(attr1ValueModified);
    
    assertNotEquals(attr1, tracked2.attribute);
    
    assertNotEquals(tracked1.attribute, tracked2.attribute);
    
    assertEquals(attr1ValueModified, tracked2.attribute);

    assertEquals(tracked1.distributed, tracked2.distributed);
    
    assertEquals(tracked1.id, tracked2.id);
  }
}

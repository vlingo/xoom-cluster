// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
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

public class AttributeSetTest {

  @Test
  public void testNamed() {
    final String name = "test";
    
    final AttributeSet set1 = AttributeSet.named(name);
    
    assertEquals(name, set1.name);
    
    assertEquals(set1, AttributeSet.named(name));
    
    assertNotEquals(set1, AttributeSet.named("test-again"));
  }

  @Test
  public void testAddIfAbsent() {
    final String name = "test";
    
    final AttributeSet set1 = AttributeSet.named(name);
    
    final Attribute<Integer> attribute1 = Attribute.from("attr1", 1);
    
    final TrackedAttribute tracked1 = set1.addIfAbsent(attribute1);
    
    assertFalse(tracked1.isAbsent());
    
    assertTrue(tracked1.isPresent());
    
    final TrackedAttribute tracked2 = set1.addIfAbsent(attribute1);
    
    assertEquals(tracked1, tracked2);
    assertTrue(tracked1 == tracked2);
    
    final Attribute<String> attribute2 = Attribute.from("attr2", "One");
    
    final TrackedAttribute tracked3 = set1.addIfAbsent(attribute2);
    
    assertNotEquals(tracked1, tracked3);
    assertNotEquals(tracked2, tracked3);
    assertEquals(tracked3, tracked3);
  }
  
  @Test
  public void testAttributeNamed() {
    final String name = "test";
    final String attrName = "attr1";
    
    final AttributeSet set1 = AttributeSet.named(name);
    
    final Attribute<Integer> attribute1 = Attribute.from(attrName, 1);
    
    final TrackedAttribute tracked1 = set1.addIfAbsent(attribute1);
    
    set1.addIfAbsent(Attribute.from(attrName+"-a", 2));
    set1.addIfAbsent(Attribute.from(attrName+"-b", 3));
    set1.addIfAbsent(Attribute.from(attrName+"-c", 4));

    final TrackedAttribute tracked2 = set1.attributeNamed(attrName);
    
    assertEquals(tracked1, tracked2);
    assertTrue(tracked1 == tracked2);
    
    final TrackedAttribute tracked3 = set1.attributeNamed(attrName + "+1");
    
    assertNotEquals(tracked1, tracked3);
    assertTrue(tracked3.isAbsent());
    assertFalse(tracked3.isPresent());
  }

  @Test
  public void testRemove() {
    final String name = "test";
    final String attrName = "attr1";
    
    final AttributeSet set1 = AttributeSet.named(name);
    
    final Attribute<Integer> attribute1 = Attribute.from(attrName, 1);
    
    final TrackedAttribute tracked1 = set1.addIfAbsent(attribute1);
    
    set1.addIfAbsent(Attribute.from(attrName+"-a", 2));
    set1.addIfAbsent(Attribute.from(attrName+"-b", 3));
    set1.addIfAbsent(Attribute.from(attrName+"-c", 4));
    
    final TrackedAttribute tracked2 = set1.remove(tracked1.attribute);
    
    assertEquals(tracked1, tracked2);
    assertTrue(tracked1 == tracked2);

    assertTrue(set1.attributeNamed(attrName+"-a").isPresent());
    assertTrue(set1.attributeNamed(attrName+"-b").isPresent());
    assertTrue(set1.attributeNamed(attrName+"-c").isPresent());
  }

  @Test
  public void testReplace() {
    final String name = "test";
    final String attrName = "attr1";
    
    final AttributeSet set1 = AttributeSet.named(name);
    
    final Attribute<Integer> attribute1 = Attribute.from(attrName, 1);
    
    final TrackedAttribute tracked1 = set1.addIfAbsent(attribute1);
    
    set1.addIfAbsent(Attribute.from(attrName+"-a", 2));
    set1.addIfAbsent(Attribute.from(attrName+"-b", 3));
    set1.addIfAbsent(Attribute.from(attrName+"-c", 4));
    
    final TrackedAttribute tracked2 = set1.replace(Attribute.from(attrName, 2));
    
    assertNotEquals(tracked1, tracked2);

    assertEquals(tracked2, set1.attributeNamed(attrName));
    
    assertTrue(set1.attributeNamed(attrName).isPresent());
    assertTrue(set1.attributeNamed(attrName+"-a").isPresent());
    assertTrue(set1.attributeNamed(attrName+"-b").isPresent());
    assertTrue(set1.attributeNamed(attrName+"-c").isPresent());
  }
}

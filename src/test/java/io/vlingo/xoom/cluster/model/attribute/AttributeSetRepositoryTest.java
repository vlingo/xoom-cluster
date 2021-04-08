// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AttributeSetRepositoryTest {
  private final AttributeSetRepository repository = new AttributeSetRepository();
  private int times = 0;
  
  @Test
  public void testAdd() {
    final AttributeSet set1 = attributeSetFixture("add");
    
    repository.add(set1);
    
    assertEquals(set1, repository.attributeSetOf("add"));
  }
  
  @Test
  public void testAll() {
    final AttributeSet set1 = attributeSetFixture("set1");
    final AttributeSet set2 = attributeSetFixture("set2");
    final AttributeSet set3 = attributeSetFixture("set3");
    
    repository.add(set1);
    repository.add(set2);
    repository.add(set3);
    
    assertEquals(3, repository.all().size());
  }

  @Test
  public void testAttributeSetOf() {
    final AttributeSet set1 = attributeSetFixture("set1");
    final AttributeSet set2 = attributeSetFixture("set2");
    final AttributeSet set3 = attributeSetFixture("set3");
    
    repository.add(set1);
    repository.add(set2);
    repository.add(set3);
    
    assertEquals(set3, repository.attributeSetOf("set3"));
    assertEquals(set2, repository.attributeSetOf("set2"));
    assertEquals(set1, repository.attributeSetOf("set1"));
  }
  
  @Test
  public void testRemove() {
    final AttributeSet set1 = attributeSetFixture("set1");
    final AttributeSet set2 = attributeSetFixture("set2");
    final AttributeSet set3 = attributeSetFixture("set3");
    
    repository.add(set1);
    repository.add(set2);
    repository.add(set3);
    
    repository.remove("set1");
    assertEquals(AttributeSet.None, repository.attributeSetOf("set1"));
    assertNotNull(repository.attributeSetOf("set2"));
    assertNotNull(repository.attributeSetOf("set3"));
    
    repository.remove("set2");
    assertEquals(AttributeSet.None, repository.attributeSetOf("set1"));
    assertEquals(AttributeSet.None, repository.attributeSetOf("set2"));
    assertNotNull(repository.attributeSetOf("set3"));
    
    repository.remove("set3");
    assertEquals(AttributeSet.None, repository.attributeSetOf("set1"));
    assertEquals(AttributeSet.None, repository.attributeSetOf("set2"));
    assertEquals(AttributeSet.None, repository.attributeSetOf("set3"));
  }
  
  private AttributeSet attributeSetFixture(final String name) {
    final AttributeSet set = AttributeSet.named(name);
    
    times = (times * 2) + 1;
    
    for (int idx = 0; idx < times; ++idx) {
      final int current = times + idx;
      
      set.addIfAbsent(Attribute.from("attr"+current, current));
    }
    
    return set;
  }
}

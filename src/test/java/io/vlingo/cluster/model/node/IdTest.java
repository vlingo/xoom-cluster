// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IdTest {

  @Test
  public void testIdCreationState() {
    final Id id = Id.of(1);
    assertEquals(Id.of(1), id);
    assertEquals(1, id.value());
    assertFalse(id.hasNoId());
    assertTrue(id.isValid()); 
  }

  @Test
  public void testIdComparisons() {
    final Id id1 = Id.of(1);
    final Id id2 = Id.of(2);
    assertNotEquals(0, id1.compareTo(id2));
    assertEquals(-1, id1.compareTo(id2));
    assertTrue(id2.greaterThan(id1));
    assertFalse(id1.greaterThan(id2));
  }
}

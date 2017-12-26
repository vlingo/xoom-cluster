// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
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

public class NameTest {

  @Test
  public void testNameCreationState() {
    final Name name1 = new Name("name1");
    final Name name2 = new Name("name2");
    assertNotEquals(name2, name1);
    assertFalse(name1.hasNoName());
    assertTrue(name1.sameAs("name1"));
    assertEquals("name2", name2.value());
  }
}

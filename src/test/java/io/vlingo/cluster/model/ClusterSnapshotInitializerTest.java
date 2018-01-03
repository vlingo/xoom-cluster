// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ClusterSnapshotInitializerTest extends AbstractClusterTest {

  @Test
  public void testCreate() throws Exception {
    final ClusterSnapshotInitializer initializer = new ClusterSnapshotInitializer("node1", properties);
    
    assertNotNull(initializer);
    assertNotNull(initializer.communicationsHub());
    assertNotNull(initializer.configuration());
    assertNotNull(initializer.localNode());
    assertEquals(1, initializer.localNode().id().value());
    assertNotNull(initializer.localNodeId());
    assertEquals(1, initializer.localNodeId().value());
    assertNotNull(initializer.registry());
  }
}

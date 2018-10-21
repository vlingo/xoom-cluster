// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Logger;
import io.vlingo.common.Tuple2;

public class ClusterTest extends AbstractClusterTest {

  @Test
  public void testClusterSnapshotControl() throws Exception {
    final Tuple2<ClusterSnapshotControl, Logger> control = Cluster.controlFor("node1");
    
    pause();
    assertNotNull(control);
    assertTrue(Cluster.isRunning());
    
    control._1.shutDown();
    pause();
    assertFalse(Cluster.isRunning());
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    this.delay = 500L;
  }
}

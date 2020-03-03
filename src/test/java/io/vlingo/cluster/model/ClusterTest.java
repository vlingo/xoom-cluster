// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.vlingo.actors.Logger;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.application.ClusterApplication.DefaultClusterApplicationInstantiator;
import io.vlingo.common.Tuple2;

public class ClusterTest extends AbstractClusterTest {
  @Test
  public void testClusterSnapshotControl() throws Exception {
    final Tuple2<ClusterSnapshotControl, Logger> control = Cluster.controlFor(World.startWithDefaults("test"), new DefaultClusterApplicationInstantiator(), properties, "node1");

    assertNotNull(control);
  }
}

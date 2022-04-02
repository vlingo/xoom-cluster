// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.cluster.model.application.ClusterApplication2.DefaultClusterApplicationInstantiator;
import io.vlingo.xoom.common.Tuple2;

public class ClusterTest extends AbstractClusterTest {
  @Test
  public void testClusterSnapshotControl() throws Exception {
    final Tuple2<ClusterControl, Logger> control = Cluster.controlFor(World.startWithDefaults("test"), new DefaultClusterApplicationInstantiator(), properties, "node1");

    assertNotNull(control);
  }
}

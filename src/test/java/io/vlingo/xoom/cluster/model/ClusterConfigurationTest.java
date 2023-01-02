// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.xoom.wire.node.Node;
import org.junit.Test;

import io.vlingo.xoom.cluster.StaticClusterConfiguration;

import static org.junit.Assert.*;

public class ClusterConfigurationTest {

  static final String localhost = "localhost";

  @Test
  public void shouldConfigureMultiNodeCluster() {
    int portSeed = 10_000 + new Random().nextInt(50_000);
    final StaticClusterConfiguration staticConfiguration = StaticClusterConfiguration.allNodes(new AtomicInteger(portSeed));

    // common
    assertEquals(Integer.valueOf(4096), staticConfiguration.properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), staticConfiguration.properties.getInteger("cluster.app.buffer.size", 0));

    assertEquals(3, staticConfiguration.allNodes.size());
    assertEquals(false, staticConfiguration.properties.singleNode());

    for (int i = 0; i < staticConfiguration.allNodes.size(); i++) {
      Node node = staticConfiguration.allNodes.get(i);

      assertEquals(i + 1, node.id().value());
      assertEquals("node" + (i + 1), node.name().value());
      assertEquals(localhost, node.operationalAddress().hostName());
      assertEquals(++portSeed, node.operationalAddress().port());
      assertEquals(localhost, node.applicationAddress().hostName());
      assertEquals(++portSeed, node.applicationAddress().port());
      assertEquals( i == 0, node.isSeed());
    }

    assertFalse(staticConfiguration.properties.getString("cluster.seeds", "").isEmpty());
  }

  @Test
  public void shouldConfigureFiveNodeCluster() {
    int portSeed = 10_000 + new Random().nextInt(50_000);
    final StaticClusterConfiguration staticConfiguration = StaticClusterConfiguration.allNodes(new AtomicInteger(portSeed), 5);

    // common
    assertEquals(Integer.valueOf(4096), staticConfiguration.properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), staticConfiguration.properties.getInteger("cluster.app.buffer.size", 0));

    assertEquals(5, staticConfiguration.allNodes.size());
    assertEquals(false, staticConfiguration.properties.singleNode());

    for (int i = 0; i < staticConfiguration.allNodes.size(); i++) {
      Node node = staticConfiguration.allNodes.get(i);

      assertEquals(i + 1, node.id().value());
      assertEquals("node" + (i + 1), node.name().value());
      assertEquals(localhost, node.operationalAddress().hostName());
      assertEquals(++portSeed, node.operationalAddress().port());
      assertEquals(localhost, node.applicationAddress().hostName());
      assertEquals(++portSeed, node.applicationAddress().port());
      assertEquals( i == 0, node.isSeed());
    }

    assertFalse(staticConfiguration.properties.getString("cluster.seeds", "").isEmpty());
  }

  @Test
  public void shouldConfigureSingleNodeCluster() {
    int portSeed = 10_000 + new Random().nextInt(50_000);
    final StaticClusterConfiguration staticConfiguration = StaticClusterConfiguration.oneNode(new AtomicInteger(portSeed));

    // common
    assertEquals(Integer.valueOf(4096), staticConfiguration.properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), staticConfiguration.properties.getInteger("cluster.app.buffer.size", 0));

    assertEquals(1, staticConfiguration.allNodes.size());
    assertEquals(true, staticConfiguration.properties.singleNode());

    for (int i = 0; i < staticConfiguration.allNodes.size(); i++) {
      Node node = staticConfiguration.allNodes.get(i);

      assertEquals(i + 1, node.id().value());
      assertEquals("node" + (i + 1), node.name().value());
      assertEquals(localhost, node.operationalAddress().hostName());
      assertEquals(++portSeed, node.operationalAddress().port());
      assertEquals(localhost, node.applicationAddress().hostName());
      assertEquals(++portSeed, node.applicationAddress().port());
      assertFalse(node.isSeed()); // on single node cluster, isSeed is not used
    }

    assertTrue(staticConfiguration.properties.getString("cluster.seeds", "").isEmpty());
  }
}

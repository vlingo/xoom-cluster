// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.xoom.wire.node.Node;
import org.junit.Test;

import io.vlingo.xoom.cluster.ClusterProperties;

import static org.junit.Assert.*;

public class ClusterPropertiesTest {

  static final String localhost = "localhost";

  @Test
  public void shouldConfigureMultiNodeCluster() {
    final ClusterProperties properties = ClusterProperties.allNodes();

    // common
    assertEquals(Integer.valueOf(4096), properties.properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), properties.properties.getInteger("cluster.app.buffer.size", 0));

    assertEquals(3, properties.allNodes.size());
    for (int i = 0; i < properties.allNodes.size(); i++) {
      Node node = properties.allNodes.get(i);

      assertEquals(i + 1, node.id().value());
      assertEquals("node" + (i + 1), node.name().value());
      assertEquals(localhost, node.operationalAddress().hostName());
      assertEquals(localhost, node.applicationAddress().hostName());
      assertEquals( i == 0, node.isSeed());
    }

    assertFalse(properties.properties.getString("cluster.seeds", "").isEmpty());
  }

  @Test
  public void shouldConfigureFiveNodeCluster() {
    final ClusterProperties properties = ClusterProperties.allNodes(new AtomicInteger(37370), 5);

    // common
    assertEquals(Integer.valueOf(4096), properties.properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), properties.properties.getInteger("cluster.app.buffer.size", 0));

    assertEquals(5, properties.allNodes.size());
    for (int i = 0; i < properties.allNodes.size(); i++) {
      Node node = properties.allNodes.get(i);

      assertEquals(i + 1, node.id().value());
      assertEquals("node" + (i + 1), node.name().value());
      assertEquals(localhost, node.operationalAddress().hostName());
      assertEquals(localhost, node.applicationAddress().hostName());
      assertEquals( i == 0, node.isSeed());
    }

    assertFalse(properties.properties.getString("cluster.seeds", "").isEmpty());
  }

  @Test
  public void shouldConfigureSingleNodeCluster() {
    final ClusterProperties properties = ClusterProperties.oneNode();

    // common
    assertEquals(Integer.valueOf(4096), properties.properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), properties.properties.getInteger("cluster.app.buffer.size", 0));

    assertEquals(1, properties.allNodes.size());
    for (int i = 0; i < properties.allNodes.size(); i++) {
      Node node = properties.allNodes.get(i);

      assertEquals(i + 1, node.id().value());
      assertEquals("node" + (i + 1), node.name().value());
      assertEquals(localhost, node.operationalAddress().hostName());
      assertEquals(localhost, node.applicationAddress().hostName());
      assertEquals( false, node.isSeed()); // on single node cluster, isSeed is not used
    }

    assertTrue(properties.properties.getString("cluster.seeds", "").isEmpty());
  }
}

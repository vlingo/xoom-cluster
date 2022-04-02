// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import io.vlingo.xoom.cluster.ClusterProperties;

public class ClusterPropertiesTest {

  @Test
  public void shouldConfigureMultiNodeCluster() {
    final Properties properties = ClusterProperties.allNodes();

    // common
    assertEquals(Integer.valueOf(4096), properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), properties.getInteger("cluster.app.buffer.size", 0));

    final String[] nodes = properties.getString("cluster.nodes", "").split(",");

    assertEquals(3, nodes.length);
    assertEquals("node1", nodes[0]);
    assertEquals("node2", nodes[1]);
    assertEquals("node3", nodes[2]);

    // node specific
    assertEquals("1", properties.getString("node.node1.id", ""));
    assertEquals("node1", properties.getString("node.node1.name", ""));
    assertEquals("localhost", properties.getString("node.node1.host", ""));

    assertEquals("2", properties.getString("node.node2.id", ""));
    assertEquals("node2", properties.getString("node.node2.name", ""));
    assertEquals("localhost", properties.getString("node.node2.host", ""));

    assertEquals("3", properties.getString("node.node3.id", ""));
    assertEquals("node3", properties.getString("node.node3.name", ""));
    assertEquals("localhost", properties.getString("node.node3.host", ""));
  }

  @Test
  public void shouldConfigureFiveNodeCluster() {
    final Properties properties = ClusterProperties.allNodes(new AtomicInteger(37370), 5);

    // common
    assertEquals(Integer.valueOf(4096), properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), properties.getInteger("cluster.app.buffer.size", 0));

    final String[] nodes = properties.getString("cluster.nodes", "").split(",");

    assertEquals(5, nodes.length);
    assertEquals("node1", nodes[0]);
    assertEquals("node2", nodes[1]);
    assertEquals("node3", nodes[2]);
    assertEquals("node4", nodes[3]);
    assertEquals("node5", nodes[4]);

    // node specific
    assertEquals("1", properties.getString("node.node1.id", ""));
    assertEquals("node1", properties.getString("node.node1.name", ""));
    assertEquals("localhost", properties.getString("node.node1.host", ""));

    assertEquals("2", properties.getString("node.node2.id", ""));
    assertEquals("node2", properties.getString("node.node2.name", ""));
    assertEquals("localhost", properties.getString("node.node2.host", ""));

    assertEquals("3", properties.getString("node.node3.id", ""));
    assertEquals("node3", properties.getString("node.node3.name", ""));
    assertEquals("localhost", properties.getString("node.node3.host", ""));

    assertEquals("4", properties.getString("node.node4.id", ""));
    assertEquals("node4", properties.getString("node.node4.name", ""));
    assertEquals("localhost", properties.getString("node.node3.host", ""));

    assertEquals("5", properties.getString("node.node5.id", ""));
    assertEquals("node5", properties.getString("node.node5.name", ""));
    assertEquals("localhost", properties.getString("node.node5.host", ""));
  }

  @Test
  public void shouldConfigureSingleNodeCluster() {
    final Properties properties = ClusterProperties.oneNode();

    // common
    assertEquals(Integer.valueOf(4096), properties.getInteger("cluster.op.buffer.size", 0));
    assertEquals(Integer.valueOf(10240), properties.getInteger("cluster.app.buffer.size", 0));

    final String[] nodes = properties.getString("cluster.nodes", "").split(",");

    assertEquals(1, nodes.length);
    assertEquals("node1", nodes[0]);

    // node specific
    assertEquals("1", properties.getString("node.node1.id", ""));
    assertEquals("node1", properties.getString("node.node1.name", ""));
    assertEquals("localhost", properties.getString("node.node1.host", ""));
  }
}

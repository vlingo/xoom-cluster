// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PropertiesTest extends AbstractClusterTest {

  @Test
  public void testApplicationBufferSize() {
    assertEquals(10240, properties.applicationBufferSize());
  }
  
  @Test
  public void testApplicationOutgoingPooledBuffers() {
    assertEquals(50, properties.applicationOutgoingPooledBuffers());
  }

//  @Test
//  public void testApplicationPort() throws Exception {
//    assertEquals(37372, properties.applicationPort("node1"));
//    assertEquals(37374, properties.applicationPort("node2"));
//    assertEquals(37376, properties.applicationPort("node3"));
//  }

  @Test
  public void testClusterApplicationClass() {
    assertNotNull(properties.clusterApplicationClassname());
    assertNotNull(properties.clusterApplicationClass());
  }

  @Test
  public void testClusterHealthCheckInterval() {
    assertEquals(2000, properties.clusterHealthCheckInterval());
  }

  @Test
  public void testClusterHeartbeatInterval() {
    assertEquals(7000, properties.clusterHeartbeatInterval());
  }

  @Test
  public void testClusterQuorumTimeout() {
    assertEquals(60000, properties.clusterQuorumTimeout());
  }

  @Test
  public void testOperationalBufferSize() {
    assertEquals(4096, properties.operationalBufferSize());
  }

  @Test
  public void testOperationalOutgoingPooledBuffers() {
    assertEquals(20, properties.operationalOutgoingPooledBuffers());
  }

  @Test
  public void testNodes() {
    final List<String> nodes = properties.nodes();
    assertEquals(3, nodes.size());
    assertTrue(nodes.contains("node1"));
    assertTrue(nodes.contains("node2"));
    assertTrue(nodes.contains("node3"));
  }

  @Test
  public void testUseSSL() {
    assertFalse(properties.useSSL());
  }

  @Test
  public void testNodes1() {
    assertEquals(1, properties.nodeId("node1"));
    assertEquals("node1", properties.nodeName("node1"));
    assertEquals("localhost", properties.host("node1"));
    assertEquals(false, properties.isSeed("node1"));
//    assertEquals(37371, properties.operationalPort("node1"));
//    assertEquals(37372, properties.applicationPort("node1"));
  }

  @Test
  public void testNodes2() {
    assertEquals(2, properties.nodeId("node2"));
    assertEquals("node2", properties.nodeName("node2"));
    assertEquals("localhost", properties.host("node2"));
    assertEquals(true, properties.isSeed("node2"));
//    assertEquals(37373, properties.operationalPort("node2"));
//    assertEquals(37374, properties.applicationPort("node2"));
  }

  @Test
  public void testNodes3() {
    assertEquals(3, properties.nodeId("node3"));
    assertEquals("node3", properties.nodeName("node3"));
    assertEquals("localhost", properties.host("node3"));
    assertEquals(false, properties.isSeed("node3"));
//    assertEquals(37375, properties.operationalPort("node3"));
//    assertEquals(37376, properties.applicationPort("node3"));
  }
}

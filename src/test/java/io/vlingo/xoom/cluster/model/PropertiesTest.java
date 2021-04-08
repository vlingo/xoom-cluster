// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
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
  public void testApplicationBufferSize() throws Exception {
    assertEquals(10240, properties.applicationBufferSize());
  }
  
  @Test
  public void testApplicationOutgoingPooledBuffers() throws Exception {
    assertEquals(50, properties.applicationOutgoingPooledBuffers());
  }

//  @Test
//  public void testApplicationPort() throws Exception {
//    assertEquals(37372, properties.applicationPort("node1"));
//    assertEquals(37374, properties.applicationPort("node2"));
//    assertEquals(37376, properties.applicationPort("node3"));
//  }

  @Test
  public void testClusterApplicationClass() throws Exception {
    assertNotNull(properties.clusterApplicationClassname());
    assertNotNull(properties.clusterApplicationClass());
  }

  @Test
  public void testClusterHealthCheckInterval() throws Exception {
    assertEquals(2000, properties.clusterHealthCheckInterval());
  }

  @Test
  public void testClusterHeartbeatInterval() throws Exception {
    assertEquals(7000, properties.clusterHeartbeatInterval());
  }

  @Test
  public void testClusterLiveNodeTimeout() throws Exception {
    assertEquals(20000, properties.clusterLiveNodeTimeout());
  }

  @Test
  public void testClusterQuorumTimeout() throws Exception {
    assertEquals(60000, properties.clusterQuorumTimeout());
  }

  @Test
  public void testOperationalBufferSize() throws Exception {
    assertEquals(4096, properties.operationalBufferSize());
  }

  @Test
  public void testOperationalOutgoingPooledBuffers() throws Exception {
    assertEquals(20, properties.operationalOutgoingPooledBuffers());
  }

  @Test
  public void testSeedNodes() throws Exception {
    final List<String> seedNodes = properties.seedNodes();
    assertEquals(3, seedNodes.size());
    assertTrue(seedNodes.contains("node1"));
    assertTrue(seedNodes.contains("node2"));
    assertTrue(seedNodes.contains("node3"));
  }

  @Test
  public void testUseSSL() throws Exception {
    assertFalse(properties.useSSL());
  }

  @Test
  public void testNodes1() throws Exception {
    assertEquals(1, properties.nodeId("node1"));
    assertEquals("node1", properties.nodeName("node1"));
    assertEquals("localhost", properties.host("node1"));
//    assertEquals(37371, properties.operationalPort("node1"));
//    assertEquals(37372, properties.applicationPort("node1"));
  }

  @Test
  public void testNodes2() throws Exception {
    assertEquals(2, properties.nodeId("node2"));
    assertEquals("node2", properties.nodeName("node2"));
    assertEquals("localhost", properties.host("node2"));
//    assertEquals(37373, properties.operationalPort("node2"));
//    assertEquals(37374, properties.applicationPort("node2"));
  }

  @Test
  public void testNodes3() throws Exception {
    assertEquals(3, properties.nodeId("node3"));
    assertEquals("node3", properties.nodeName("node3"));
    assertEquals("localhost", properties.host("node3"));
//    assertEquals(37375, properties.operationalPort("node3"));
//    assertEquals(37376, properties.applicationPort("node3"));
  }
}

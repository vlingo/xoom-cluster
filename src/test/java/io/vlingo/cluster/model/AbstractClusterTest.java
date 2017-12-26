// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.testkit.TestWorld;

public abstract class AbstractClusterTest extends AbstractMessageTool {
  protected MockClusterApplication application;
  protected ClusterConfiguration config;
  protected long delay = 100L;
  protected Properties properties;
  protected TestWorld testWorld;
  
  @Test
  public void testValues() throws Exception {
    assertNotNull(application);
    assertNotNull(config);
    assertNotNull(properties);
    assertNotNull(testWorld);
  }
  
  protected void pause() {
    try { Thread.sleep(delay); } catch (Exception e) { }
  }
  
  @Before
  public void setUp() throws Exception {
    java.util.Properties properties = new java.util.Properties();
    
    properties.setProperty("cluster.ssl", "false");
    
    properties.setProperty("cluster.op.buffer.size", "4096");
    properties.setProperty("cluster.app.buffer.size", "10240");
    properties.setProperty("cluster.op.outgoing.pooled.buffers", "20");
    properties.setProperty("cluster.app.outgoing.pooled.buffers", "50");
    
    properties.setProperty("cluster.msg.charset", "UTF-8");
    
    properties.setProperty("cluster.app.class", "io.vlingo.cluster.model.application.FakeClusterApplicationActor");
    
    properties.setProperty("cluster.health.check.interval", "2000");
    properties.setProperty("cluster.live.node.timeout", "20000");
    properties.setProperty("cluster.heartbeat.interval", "7000");
    properties.setProperty("cluster.quorum.timeout", "60000");
    
    properties.setProperty("cluster.seedNodes", "node1,node2,node3");
    
    properties.setProperty("node.node1.id", "1");
    properties.setProperty("node.node1.name", "node1");
    properties.setProperty("node.node1.host", "localhost");
    properties.setProperty("node.node1.op.port", "37371");
    properties.setProperty("node.node1.app.port", "37372");
    
    properties.setProperty("node.node2.id", "2");
    properties.setProperty("node.node2.name", "node2");
    properties.setProperty("node.node2.host", "localhost");
    properties.setProperty("node.node2.op.port", "37373");
    properties.setProperty("node.node2.app.port", "37374");
    
    properties.setProperty("node.node3.id", "3");
    properties.setProperty("node.node3.name", "node3");
    properties.setProperty("node.node3.host", "localhost");
    properties.setProperty("node.node3.op.port", "37375");
    properties.setProperty("node.node3.app.port", "37376");
    
    this.properties = Properties.openForTest(properties);
    
    this.config = new ClusterConfiguration(this.properties);
    
    this.testWorld = TestWorld.start("cluster-test-world");
    
    this.application = new MockClusterApplication();
  }
}

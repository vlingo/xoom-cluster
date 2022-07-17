// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.vlingo.xoom.wire.node.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.actors.testkit.TestWorld;

public abstract class AbstractClusterTest extends AbstractMessageTool {
  private static final Random random = new Random();
  private static final AtomicInteger PORT_TO_USE = new AtomicInteger(10_000 + random.nextInt(50_000));

  protected MockClusterApplication application;
  protected ClusterConfiguration config;
  protected Node localNode;
  protected List<Node> allNodes;
  protected Properties properties;
  protected TestWorld testWorld;

  @Test
  public void testValues() {
    assertNotNull(application);
    assertNotNull(config);
    assertNotNull(properties);
    assertNotNull(testWorld);
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

    properties.setProperty("cluster.app.class", "io.vlingo.xoom.cluster.model.application.FakeClusterApplicationActor");
    properties.setProperty("cluster.app.stage", "fake.app.stage");

    properties.setProperty("cluster.health.check.interval", "2000");

    properties.setProperty("cluster.nodes.quorum", "2");
    properties.setProperty("cluster.nodes", "node1,node2,node3");

    properties.setProperty("node.node1.id", "1");
    properties.setProperty("node.node1.name", "node1");
    properties.setProperty("node.node1.host", "localhost");
    properties.setProperty("node.node1.op.port", nextPortToUseString());
    properties.setProperty("node.node1.app.port", nextPortToUseString());

    properties.setProperty("node.node2.id", "2");
    properties.setProperty("node.node2.name", "node2");
    properties.setProperty("node.node2.host", "localhost");
    properties.setProperty("node.node2.op.port", nextPortToUseString());
    properties.setProperty("node.node2.app.port", nextPortToUseString());
    properties.setProperty("node.node2.seed", "true");

    properties.setProperty("node.node3.id", "3");
    properties.setProperty("node.node3.name", "node3");
    properties.setProperty("node.node3.host", "localhost");
    properties.setProperty("node.node3.op.port", nextPortToUseString());
    properties.setProperty("node.node3.app.port", nextPortToUseString());

    this.properties = Properties.openForTest(properties, "node1");

    this.testWorld = TestWorld.startWithDefaults("cluster-test-world");

    this.config = new ClusterConfiguration("node1", this.properties, testWorld.defaultLogger());

    this.allNodes = Arrays.asList(nodeFrom("node1", this.properties),
            nodeFrom("node2", this.properties),
            nodeFrom("node3", this.properties));

    this.application = new MockClusterApplication();
  }

  @After
  public void tearDown() {
    testWorld.terminate();
  }

  private int nextPortToUse() {
    return PORT_TO_USE.incrementAndGet();
  }

  private String nextPortToUseString() {
    return "" + nextPortToUse();
  }

  private Node nodeFrom(String nodeName, Properties properties) {
    Host host = Host.of(properties.getStringForNode(nodeName, "host", ""));
    return new Node(Id.of(properties.getIntegerForNode(nodeName, "id", -1)),
            Name.of(nodeName),
            Address.from(host, properties.getIntegerForNode(nodeName, "op.port", -1), AddressType.OP),
            Address.from(host, properties.getIntegerForNode(nodeName, "app.port", -1), AddressType.APP),
            properties.isSeed(nodeName));
  }

  protected List<Node> allOtherNodes() {
    return allNodes.stream()
            .filter(n -> !n.id().equals(config.localNode().id()))
            .collect(Collectors.toList());
  }

  protected Node nextNodeWith(final int nodeNumber, boolean seed) {
    return Node.with(Id.of(nodeNumber), Name.of("node" + nodeNumber), Host.of("localhost"), nextPortToUse(), nextPortToUse(), seed);
  }
}

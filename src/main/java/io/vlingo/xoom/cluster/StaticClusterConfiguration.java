// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster;

import io.vlingo.xoom.cluster.model.NodeProperties;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.wire.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Properties that are predefined for single-node and multi-node clusters. These
 * are useful for test, but provided in {@code java/main} for access by tests
 * outside of {@code xoom-cluster}.
 */
public class StaticClusterConfiguration {
  private static final String DefaultApplicationClassname = "io.vlingo.xoom.cluster.model.application.FakeClusterApplicationActor";
  private static final Random random = new Random();
  private static final AtomicInteger PORT_TO_USE = new AtomicInteger(10_000 + random.nextInt(50_000));

  public final Properties properties;
  public final List<Node> allNodes; // static configuration of nodes; dynamic configuration of nodes is provided by Registry

  private StaticClusterConfiguration(Properties properties, List<Node> allNodes) {
    this.properties = properties;
    this.allNodes = allNodes;
  }

  public static StaticClusterConfiguration allNodes() {
    return allNodes(PORT_TO_USE);
  }

  public static StaticClusterConfiguration allNodes(final AtomicInteger portSeed) {
    return allNodes(portSeed, 3);
  }

  public static StaticClusterConfiguration allNodes(final AtomicInteger portSeed, final int totalNodes) {
    return allNodes(portSeed, totalNodes, DefaultApplicationClassname);
  }

  public static StaticClusterConfiguration allNodes(final AtomicInteger portSeed, final int totalNodes, final String applicationClassname) {
    java.util.Properties common = common(totalNodes, applicationClassname);
    List<Node> allNodes = allNodes(totalNodes, portSeed);
    if (totalNodes > 1) {
      Address operationalAddress = allNodes.get(0).operationalAddress();
      common.setProperty("cluster.seeds", operationalAddress.full());
    }

    return new StaticClusterConfiguration(Properties.openWith(common), allNodes);
  }

  public static StaticClusterConfiguration oneNode() {
    return oneNode(PORT_TO_USE);
  }

  public static StaticClusterConfiguration oneNode(final AtomicInteger portSeed) {
    return oneNode(portSeed, DefaultApplicationClassname);
  }

  public static StaticClusterConfiguration oneNode(final AtomicInteger portSeed, final String applicationClassname) {
    final Properties properties = Properties.openWith(common(1, applicationClassname));
    final List<Node> allNodes = allNodes(1, portSeed);

    return new StaticClusterConfiguration(properties, allNodes);
  }

  private static List<Node> allNodes(final int totalNodes, final AtomicInteger portSeed) {
    List<Node> nodes = new ArrayList<>();
    for (int i = 1; i <= totalNodes; i++) {
      final Name nodeName = Name.of("node" + i);
      final boolean isSeed = totalNodes > 1 && i == 1; // only node1 when multi node configuration
      final Host localhost = Host.of("localhost");

      nodes.add(new Node(Id.of(i), nodeName, Address.from(localhost, nextPortToUse(portSeed), AddressType.OP),
              Address.from(localhost, nextPortToUse(portSeed), AddressType.APP), isSeed));
    }

    return nodes;
  }

  private static java.util.Properties common(final int totalNodes, final String applicationClassname) {
    final java.util.Properties properties = new java.util.Properties();
    properties.setProperty("cluster.ssl", "false");

    properties.setProperty("cluster.op.buffer.size", "4096");
    properties.setProperty("cluster.app.buffer.size", "10240");
    properties.setProperty("cluster.op.outgoing.pooled.buffers", "20");
    properties.setProperty("cluster.app.outgoing.pooled.buffers", "50");

    properties.setProperty("cluster.msg.charset", "UTF-8");

    properties.setProperty("cluster.app.class", applicationClassname);
    properties.setProperty("cluster.app.stage", "fake.app.stage");

    properties.setProperty("cluster.health.check.interval", "2000");

    if (totalNodes > 1) {
      int quorum = totalNodes / 2 + 1;
      properties.setProperty("cluster.nodes.quorum", Integer.toString(quorum));
    }

    return properties;
  }

  private static int nextPortToUse(final AtomicInteger portSeed) {
    return portSeed.incrementAndGet();
  }

  public String propertiesOf(int nodeIndex) {
    return NodeProperties.from(allNodes.get(nodeIndex))
            .asText();
  }
}

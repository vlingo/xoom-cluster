// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Properties that are predefined for single-node and multi-node clusters. These
 * are useful for test, but provided in {@code java/main} for access by tests
 * outside of {@code xoom-cluster}.
 */
public class ClusterProperties {
  private static final String DefaultApplicationClassname = "io.vlingo.xoom.cluster.model.application.FakeClusterApplicationActor";
  private static final Random random = new Random();
  private static final AtomicInteger PORT_TO_USE = new AtomicInteger(10_000 + random.nextInt(50_000));

  public static io.vlingo.xoom.cluster.model.Properties allNodes() {
    return allNodes(PORT_TO_USE);
  }

  public static io.vlingo.xoom.cluster.model.Properties allNodes(final AtomicInteger portSeed) {
    return allNodes(PORT_TO_USE, 3);
  }

  public static io.vlingo.xoom.cluster.model.Properties allNodes(final AtomicInteger portSeed, final int totalNodes) {
    return allNodes(PORT_TO_USE, totalNodes, DefaultApplicationClassname);
  }

  public static io.vlingo.xoom.cluster.model.Properties allNodes(final AtomicInteger portSeed, final int totalNodes, final String applicationClassname) {
    java.util.Properties properties = new java.util.Properties();

    properties = common(allOf(properties, totalNodes, portSeed), totalNodes, applicationClassname);

    final io.vlingo.xoom.cluster.model.Properties clusterProperties =
            io.vlingo.xoom.cluster.model.Properties.openWith(properties);

    return clusterProperties;
  }

  public static io.vlingo.xoom.cluster.model.Properties oneNode() {
    return oneNode(PORT_TO_USE);
  }

  public static io.vlingo.xoom.cluster.model.Properties oneNode(final AtomicInteger portSeed) {
    return oneNode(portSeed, DefaultApplicationClassname);
  }

  public static io.vlingo.xoom.cluster.model.Properties oneNode(final AtomicInteger portSeed, final String applicationClassname) {
    java.util.Properties properties = new java.util.Properties();

    properties = common(oneOnly(properties, portSeed), 1, applicationClassname);

    final io.vlingo.xoom.cluster.model.Properties clusterProperties =
            io.vlingo.xoom.cluster.model.Properties.openWith(properties);

    return clusterProperties;
  }

  private static java.util.Properties oneOnly(final java.util.Properties properties, final AtomicInteger portSeed) {

    return allOf(properties, 1, portSeed);
  }

  private static java.util.Properties allOf(final java.util.Properties properties, final int totalNodes, final AtomicInteger portSeed) {
    final StringBuilder build = new StringBuilder();

    for (int idx = 1; idx <= totalNodes; ++idx) {
      final String node = "node" + idx;

      if (idx > 1) {
        build.append(",");
      }

      build.append(node);

      final String nodePropertyName = "node." + node;

      properties.setProperty(nodePropertyName + ".id", "" + idx);
      properties.setProperty(nodePropertyName + ".name", node);
      properties.setProperty(nodePropertyName + ".host", "localhost");
      properties.setProperty(nodePropertyName + ".op.port", nextPortToUseString(portSeed));
      properties.setProperty(nodePropertyName + ".app.port", nextPortToUseString(portSeed));
    }

    properties.setProperty("cluster.seedNodes", build.toString());

    return properties;
  }

  private static java.util.Properties common(final java.util.Properties properties, final int totalNodes, final String applicationClassname) {
    properties.setProperty("cluster.ssl", "false");

    properties.setProperty("cluster.op.buffer.size", "4096");
    properties.setProperty("cluster.app.buffer.size", "10240");
    properties.setProperty("cluster.op.outgoing.pooled.buffers", "20");
    properties.setProperty("cluster.app.outgoing.pooled.buffers", "50");

    properties.setProperty("cluster.msg.charset", "UTF-8");

    properties.setProperty("cluster.app.class", applicationClassname);
    properties.setProperty("cluster.app.stage", "fake.app.stage");

    properties.setProperty("cluster.health.check.interval", "2000");
    properties.setProperty("cluster.live.node.timeout", "20000");
    properties.setProperty("cluster.heartbeat.interval", "7000");
    properties.setProperty("cluster.quorum.timeout", "60000");

    return properties;
  }

  private static int nextPortToUse(final AtomicInteger portSeed) {
    return portSeed.incrementAndGet();
  }

  private static String nextPortToUseString(final AtomicInteger portSeed) {
    return "" + nextPortToUse(portSeed);
  }
}

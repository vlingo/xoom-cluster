// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vlingo.actors.Actor;

public final class Properties {
  public static final Properties instance;

  private static final String propertiesFile = "/vlingo-cluster.properties";

  static {
    instance = open();
  }

  private final java.util.Properties properties;

  public static Properties open() {
    final java.util.Properties properties = new java.util.Properties();

    try {
      properties.load(Properties.class.getResourceAsStream(propertiesFile));
    } catch (IOException e) {
      throw new IllegalStateException("Must provide properties file on classpath: " + propertiesFile);
    }

    return new Properties(properties);
  }

  public static Properties openForTest(java.util.Properties properties) {
    return new Properties(properties);
  }

  public int applicationBufferSize() {
    return getInteger("cluster.app.buffer.size", 10240);
  }

  public long applicationInboundProbeInterval() {
    final int probeInterval = getInteger("cluster.app.incoming.probe.interval", 100);

    if (probeInterval == 0) {
      throw new IllegalStateException("Must assign an application (app) incoming probe interval in properties file.");
    }

    return probeInterval;
  }

  public int applicationOutgoingPooledBuffers() {
    final int pooledBuffers = getInteger("cluster.app.outgoing.pooled.buffers", 50);

    if (pooledBuffers == 0) {
      throw new IllegalStateException("Must assign an application (app) pooled buffers size in properties file.");
    }

    return pooledBuffers;
  }

  public int applicationPort(String nodeName) {
    final int port = getInteger(nodeName, "app.port", 0);

    if (port == 0) {
      throw new IllegalStateException("Must assign an application (app) port to node '"
          + nodeName + "' in properties file.");
    }

    return port;
  }

  @SuppressWarnings("unchecked")
  public Class<Actor> clusterApplicationClass() {
    final String classname = clusterApplicationClassname();

    try {
      return (Class<Actor>) Class.forName(classname);
    } catch (Exception e) {
      throw new IllegalStateException("Must define property: cluster.app.class", e);
    }
  }

  public final String clusterApplicationClassname() {
    final String classname = getString("cluster.app.class", "");

    if (classname.length() == 0) {
      throw new IllegalStateException("Must assign a cluster app class in properties file.");
    }

    return classname;
  }

  public final String clusterApplicationStageName() {
    final String name = getString("cluster.app.stage", "");

    if (name.length() == 0) {
      throw new IllegalStateException("Must assign a cluster app stage name in properties file.");
    }

    return name;
  }

  public long clusterAttributesRedistributionInterval() {
    return getInteger("cluster.attributes.redistribution.interval", 1000);
  }

  public int clusterAttributesRedistributionRetries() {
    return getInteger("cluster.attributes.redistribution.retries", 10);
  }

  public long clusterHealthCheckInterval() {
    return getInteger("cluster.health.check.interval", 3000);
  }

  public long clusterHeartbeatInterval() {
    return getInteger("cluster.heartbeat.interval", 7000);
  }

  public long clusterLiveNodeTimeout() {
    return getInteger("cluster.live.node.timeout", 20000);
  }

  public long clusterQuorumTimeout() {
    return getInteger("cluster.quorum.timeout", 60000);
  }

  public final String host(String nodeName) {
    final String host = getString(nodeName, "host", "");

    if (host.length() == 0) {
      throw new IllegalStateException("Must assign a host to node '"
          + nodeName + "' in properties file.");
    }

    return host;
  }

  public short nodeId(String nodeName) {
    final int nodeId = getInteger(nodeName, "id", -1);

    if (nodeId == -1) {
      throw new IllegalStateException("Must assign an id to node '"
          + nodeName + "' in properties file.");
    }

    return (short) nodeId;
  }

  public final String nodeName(String nodeName) {
    final String name = getString(nodeName, "name", "");

    if (name.length() == 0) {
      throw new IllegalStateException("Must assign a name to node '"
          + nodeName + "' in properties file.");
    }

    return name;
  }

  public int operationalBufferSize() {
    return getInteger("cluster.op.buffer.size", 4096);
  }

  public long operationalInboundProbeInterval() {
    final int probeInterval = getInteger("cluster.op.incoming.probe.interval", 100);

    if (probeInterval == 0) {
      throw new IllegalStateException("Must assign an operational (op) incoming probe interval in properties file.");
    }

    return probeInterval;
  }

  public int operationalOutgoingPooledBuffers() {
    final int pooledBuffers = getInteger("cluster.op.outgoing.pooled.buffers", 20);

    if (pooledBuffers == 0) {
      throw new IllegalStateException("Must assign an operational (op) pooled buffers size in properties file.");
    }

    return pooledBuffers;
  }

  public int operationalPort(String nodeName) {
    final int port = getInteger(nodeName, "op.port", 0);

    if (port == 0) {
      throw new IllegalStateException("Must assign an operational (op) port to node '"
          + nodeName + "' in properties file.");
    }

    return port;
  }

  public final List<String> seedNodes() {
    final List<String> seedNodes = new ArrayList<String>();

    final String commaSeparated = getString("cluster.seedNodes", "");

    if (commaSeparated.length() == 0) {
      throw new IllegalStateException("Must declare seed nodes in properties file.");
    }

    for (final String seedNode : commaSeparated.split(",")) {
      seedNodes.add(seedNode.trim());
    }

    return seedNodes;
  }

  public boolean useSSL() {
    return getBoolean("cluster.ssl", false);
  }

  public final Boolean getBoolean(final String nodeName, final String key, final Boolean defaultValue) {
    final String value = getString(nodeName, key, defaultValue.toString());
    return Boolean.parseBoolean(value);
  }

  public final Boolean getBoolean(final String key, final Boolean defaultValue) {
    return getBoolean("", key, defaultValue);
  }

  public Float getFloat(final String nodeName, final String key, final Float defaultValue) {
    final String value = getString(nodeName, key, defaultValue.toString());
    return Float.parseFloat(value);
  }

  public final Float getFloat(final String key, final Float defaultValue) {
    return getFloat("", key, defaultValue);
  }

  public final Integer getInteger(final String nodeName, final String key, final Integer defaultValue) {
    final String value = getString(nodeName, key, defaultValue.toString());
    return Integer.parseInt(value);
  }

  public final Integer getInteger(final String key, final Integer defaultValue) {
    return getInteger("", key, defaultValue);
  }

  public final String getString(final String nodeName, final String key, final String defaultValue) {
    return properties.getProperty(key(nodeName, key), defaultValue);
  }

  public final String getString(final String key, final String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public void validateRequired(final String nodeName) {
    // assertions in each accessor

    nodeName(nodeName);

    nodeId(nodeName);

    host(nodeName);

    operationalPort(nodeName);

    applicationPort(nodeName);

    seedNodes();

    clusterApplicationClassname();
  }

  private Properties(java.util.Properties properties) {
    this.properties = properties;
  }

  private String key(final String nodeName, final String key) {
    if (nodeName == null || nodeName.length() == 0) {
      return key;
    }

    return "node." + nodeName + "." + key;
  }
}

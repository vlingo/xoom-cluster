// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.wire.node.*;

public final class Properties {
  private static Properties instance;

  private static final String propertiesFile = "/xoom-cluster.properties";
  private static final String propertiesFileLocation = "src/main/resources" + propertiesFile;

  private final java.util.Properties properties;

  public static Properties instance() {
    if (instance == null) {
      instance = open();
    }
    return instance;
  }

  public static Properties open() {
    Properties properties = openQuietly();

    if (properties == null) {
      System.out.println("WARNING: Missing file: " + propertiesFileLocation + " -- create or use ClusterProperties.");

      return new Properties(new java.util.Properties());
    }

    return properties;
  }

  public static Properties openQuietly() {
    final java.util.Properties properties = new java.util.Properties();

    try {
      properties.load(Properties.class.getResourceAsStream(propertiesFile));
    } catch (Throwable t) {
      return null;
    }

    return new Properties(properties);
  }

  public static Properties openWith(java.util.Properties properties) {
    instance = new Properties(properties);
    return instance;
  }

  public static Properties openForTest(java.util.Properties properties) {
    return openWith(properties);
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

  @SuppressWarnings("unchecked")
  public Class<Actor> clusterApplicationClass() {
    final String classname = clusterApplicationClassname();

    try {
      return (Class<Actor>) Class.forName(classname);
    } catch (Exception e) {
      throw new IllegalStateException("Must define property: cluster.app.class", e);
    }
  }

  public String clusterApplicationClassname() {
    final String classname = getString("cluster.app.class", "");

    if (classname.length() == 0) {
      throw new IllegalStateException("Must assign a cluster app class in properties file.");
    }

    return classname;
  }

  public String clusterApplicationStageName() {
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

  public int clusterQuorum() {
    return getInteger("cluster.nodes.quorum", 1);
  }

  public long clusterStartupPeriod() {
    return getInteger("cluster.startup.period", 5000);
  }

  public int operationalBufferSize() {
    return getInteger("cluster.op.buffer.size", 4096);
  }

  public int operationalOutgoingPooledBuffers() {
    final int pooledBuffers = getInteger("cluster.op.outgoing.pooled.buffers", 20);

    if (pooledBuffers == 0) {
      throw new IllegalStateException("Must assign an operational (op) pooled buffers size in properties file.");
    }

    return pooledBuffers;
  }

  /**
   * Gets the configured seeds from the cluster.
   *
   * @return
   */
  public List<SeedNode> seeds() {
    final List<SeedNode> seeds = new ArrayList<>();
    final String commaSeparated = getString("cluster.seeds", "");

    if (!commaSeparated.isEmpty()) {
      for (final String seedNodeProperties : commaSeparated.split(",")) {
        seeds.add(SeedNode.from(seedNodeProperties));
      }
    }

    return seeds;
  }

  /**
   * Checks whether this is a single node configuration.
   *
   * @return
   */
  public boolean singleNode() {
    return seeds().size() == 0; // || clusterQuorum() == 1;
  }

  public boolean useSSL() {
    return getBoolean("cluster.ssl", false);
  }

  public Boolean getBoolean(final String key, final Boolean defaultValue) {
    final String value = getString(key, defaultValue.toString());
    return Boolean.parseBoolean(value);
  }

  public Float getFloat(final String key, final Float defaultValue) {
    final String value = getString(key, defaultValue.toString());
    return Float.parseFloat(value);
  }

  public Integer getInteger(final String key, final Integer defaultValue) {
    final String value = getString(key, defaultValue.toString());
    return Integer.parseInt(value);
  }

  public String getString(final String key, final String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public void validateRequired() {
    // assertions in each accessor

    clusterApplicationClassname();
  }

  private Properties(java.util.Properties properties) {
    this.properties = properties;
  }
}

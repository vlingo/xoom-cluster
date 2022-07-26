// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.wire.node.*;

import java.util.function.Supplier;

/**
 * Dynamic node properties exchanged between cluster nodes.
 */
public class NodeProperties {

  public static NodeProperties from(Node node) {
    return new NodeProperties(
            node.id().value(),
            node.name().value(),
            node.isSeed(),
            node.operationalAddress().hostName(),
            node.operationalAddress().port(),
            node.applicationAddress().hostName(),
            node.applicationAddress().port());
  }

  /**
   * Creates a {@code NodeProperties} instance based on a comma separated list of properties.
   *
   * @param nodePropertiesText Comma separated list of node properties in the form of 'id:name:isSeed:host:operationalPort:applicationPort'
   * @return
   */
  public static NodeProperties from(String nodePropertiesText) {
    final String[] nodePropertiesValues = nodePropertiesText.split(":");
    if (nodePropertiesValues.length != 6) {
      throw new IllegalArgumentException("Invalid node properties! Expected format: 'id:name:isSeed:host:operationalPort:applicationPort'");
    }

    final short id = uncheckedGet(() -> Short.parseShort(nodePropertiesValues[0]), "Invalid 'nodeId' property.");
    final String name = nodePropertiesValues[1];
    final boolean isSeed = uncheckedGet(() -> Boolean.parseBoolean(nodePropertiesValues[2]), "Invalid 'isSeed' property.");
    final String host = nodePropertiesValues[3];
    final int operationalPort = uncheckedGet(() -> Integer.parseInt(nodePropertiesValues[4]), "Invalid 'operationalPort' property.");
    final int applicationPort = uncheckedGet(() -> Integer.parseInt(nodePropertiesValues[5]), "Invalid 'applicationPort' property.");

    return new NodeProperties(id, name, isSeed, host, operationalPort, host, applicationPort);
  }

  private static <T> T uncheckedGet(Supplier<T> getAction, String errorMessage) {
    try {
      return getAction.get();
    } catch (Exception e) {
      throw new IllegalArgumentException(errorMessage + " Cause: " + e.getMessage());
    }
  }

  private short id;
  private String name;
  private boolean isSeed;

  private String operationalHost;
  private int operationalPort;

  private String applicationHost;
  private int applicationPort;

  public NodeProperties() {
    // Java bean
  }

  public NodeProperties(short id, String name, boolean isSeed, String operationalHost, int operationalPort, String applicationHost, int applicationPort) {
    this.id = id;
    this.name = name;
    this.isSeed = isSeed;
    this.operationalHost = operationalHost;
    this.operationalPort = operationalPort;
    this.applicationHost = applicationHost;
    this.applicationPort = applicationPort;
  }

  public short getId() {
    return id;
  }

  public void setId(short id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isSeed() {
    return isSeed;
  }

  public void setSeed(boolean seed) {
    isSeed = seed;
  }

  public String getOperationalHost() {
    return operationalHost;
  }

  public void setOperationalHost(String operationalHost) {
    this.operationalHost = operationalHost;
  }

  public int getOperationalPort() {
    return operationalPort;
  }

  public void setOperationalPort(int operationalPort) {
    this.operationalPort = operationalPort;
  }

  public String getApplicationHost() {
    return applicationHost;
  }

  public void setApplicationHost(String applicationHost) {
    this.applicationHost = applicationHost;
  }

  public int getApplicationPort() {
    return applicationPort;
  }

  public void setApplicationPort(int applicationPort) {
    this.applicationPort = applicationPort;
  }

  public Node asNode() {
    Address operationalAddress = Address.from(Host.of(operationalHost), operationalPort, AddressType.OP);
    Address applicationAddress = Address.from(Host.of(applicationHost), applicationPort, AddressType.APP);

    return new Node(Id.of(id), Name.of(name), this.isSeed, operationalAddress, applicationAddress);
  }

  public String asText() {
    return id + ":" + name + ":" + isSeed + ":" + operationalHost + ":" + operationalPort + ":" + applicationPort;
  }
}

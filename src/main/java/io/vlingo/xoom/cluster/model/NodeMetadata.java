// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.wire.node.*;

/**
 * Node metadata information exchanged between cluster nodes.
 */
public class NodeMetadata {
  private short id;
  private String name;

  private String operationalHost;
  private int operationalPort;

  private String applicationHost;
  private int applicationPort;

  private boolean isSeed;

  public static NodeMetadata from(Node node) {
    return new NodeMetadata(
            node.id().value(),
            node.name().value(),
            node.operationalAddress().hostName(),
            node.operationalAddress().port(),
            node.applicationAddress().hostName(),
            node.applicationAddress().port(),
            node.isSeed());
  }

  public NodeMetadata() {
    // Java bean
  }

  public NodeMetadata(short id, String name, String operationalHost, int operationalPort, String applicationHost, int applicationPort, boolean isSeed) {
    this.id = id;
    this.name = name;
    this.operationalHost = operationalHost;
    this.operationalPort = operationalPort;
    this.applicationHost = applicationHost;
    this.applicationPort = applicationPort;
    this.isSeed = isSeed;
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

  public boolean isSeed() {
    return isSeed;
  }

  public void setSeed(boolean seed) {
    isSeed = seed;
  }

  public Node asNode() {
    Address operationalAddress = Address.from(Host.of(operationalHost), operationalPort, AddressType.OP);
    Address applicationAddress = Address.from(Host.of(applicationHost), applicationPort, AddressType.APP);

    return new Node(Id.of(id), Name.of(name), operationalAddress, applicationAddress, this.isSeed());
  }
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.vlingo.actors.Logger;
import io.vlingo.cluster.model.node.Address;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;
import io.vlingo.cluster.model.node.Node;

public class ClusterConfiguration implements Configuration {
  private final Logger logger;
  private final Set<Node> nodes;

  public ClusterConfiguration(final Logger logger) {
    this.logger = logger;
    this.nodes = new TreeSet<Node>();

    initializeConfiguredNodeEntries(Properties.instance);
  }

  public Collection<Node> allConfiguredNodes() {
    return Collections.unmodifiableSet(nodes);
  }

  public final Set<Node> allOtherConfiguredNodes(Id nodeId) {
    final Set<Node> except = new TreeSet<Node>();

    for (final Node node : nodes) {
      if (!node.id().equals(nodeId)) {
        except.add(node);
      }
    }

    return except;
  }

  public final Set<Node> allGreaterConfiguredNodes(Id nodeId) {
    final Set<Node> greater = new TreeSet<Node>();

    for (final Node node : nodes) {
      if (node.id().greaterThan(nodeId)) {
        greater.add(node);
      }
    }

    return greater;
  }

  public Collection<String> allConfiguredNodeNames() {
    final List<String> names = new ArrayList<String>();

    for (final Node node : nodes) {
      names.add(node.name().value());
    }

    return names;
  }

  public final Node configuredNodeMatching(Id nodeId) {
    for (final Node node : nodes) {
      if (node.id().equals(nodeId)) {
        return node;
      }
    }
    return Node.NO_NODE;
  }

  public final Id greatestConfiguredNodeId() {
    Id greatest = Id.NO_ID;

    for (final Node node : nodes) {
      if (node.id().greaterThan(greatest)) {
        greatest = node.id();
      }
    }

    return greatest;
  }

  public boolean hasConfiguredNode(Id nodeId) {
    for (final Node node : nodes) {
      if (node.id().equals(nodeId)) {
        return true;
      }
    }
    return false;
  }

  public int totalConfiguredNodes() {
    return nodes.size();
  }

  @Override
  public Logger logger() {
    return logger;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != ClusterConfiguration.class) {
      return false;
    }

    return this.nodes.equals(((ClusterConfiguration) other).nodes);
  }

  @Override
  public int hashCode() {
    return 31 * nodes.hashCode();
  }

  @Override
  public String toString() {
    return "ConfiguredCluster[" + nodes + "]";
  }

  protected ClusterConfiguration(Properties properties, final Logger logger) {
    this.logger = logger;
    this.nodes = new TreeSet<Node>();

    initializeConfiguredNodeEntries(properties);
  }

  private void initializeConfiguredNodeEntries(final Properties properties) {
    for (String configuredNodeName : Properties.instance.seedNodes()) {
      final Id nodeId = Id.of(properties.nodeId(configuredNodeName));
      final Name nodeName = new Name(configuredNodeName);
      final String host = properties.host(configuredNodeName);
      final Address opNodeAddress = Address.from(host, properties.operationalPort(configuredNodeName), AddressType.OP);
      final Address appNodeAddress = Address.from(host, properties.applicationPort(configuredNodeName), AddressType.APP);

      nodes.add(new Node(nodeId, nodeName, opNodeAddress, appNodeAddress));
    }
  }
}

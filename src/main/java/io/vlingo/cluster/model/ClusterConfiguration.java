// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import io.vlingo.actors.Logger;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Configuration;
import io.vlingo.wire.node.Host;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class ClusterConfiguration implements Configuration {
  private final Logger logger;
  private final Set<Node> nodes;

  public ClusterConfiguration(Properties properties, final Logger logger) {
    this.logger = logger;
    this.nodes = new TreeSet<Node>();

    initializeConfiguredNodeEntries(properties);
  }

  @Override
  public Set<Node> allNodes() {
    return Collections.unmodifiableSet(nodes);
  }

  @Override
  public Set<Node> allNodesOf(final Collection<Id> ids) {
    // Currently not used

    final Set<Node> nodes = new TreeSet<Node>();

    return nodes;
  }

  @Override
  public final Set<Node> allOtherNodes(Id nodeId) {
    final Set<Node> except = new TreeSet<Node>();

    for (final Node node : nodes) {
      if (!node.id().equals(nodeId)) {
        except.add(node);
      }
    }

    return except;
  }

  @Override
  public Set<Id> allOtherNodesId(final Id nodeId) {
    final Set<Id> ids = new TreeSet<Id>();

    for (final Node node : allOtherNodes(nodeId)) {
      ids.add(node.id());
    }

    return ids;
  }

  @Override
  public final Set<Node> allGreaterNodes(Id nodeId) {
    final Set<Node> greater = new TreeSet<Node>();

    for (final Node node : nodes) {
      if (node.id().greaterThan(nodeId)) {
        greater.add(node);
      }
    }

    return greater;
  }

  @Override
  public Set<String> allNodeNames() {
    final Set<String> names = new TreeSet<String>();

    for (final Node node : nodes) {
      names.add(node.name().value());
    }

    return names;
  }

  @Override
  public final Node nodeMatching(Id nodeId) {
    for (final Node node : nodes) {
      if (node.id().equals(nodeId)) {
        return node;
      }
    }
    return Node.NO_NODE;
  }

  @Override
  public final Id greatestNodeId() {
    Id greatest = Id.NO_ID;

    for (final Node node : nodes) {
      if (node.id().greaterThan(greatest)) {
        greatest = node.id();
      }
    }

    return greatest;
  }

  @Override
  public boolean hasNode(Id nodeId) {
    for (final Node node : nodes) {
      if (node.id().equals(nodeId)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int totalNodes() {
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

  private void initializeConfiguredNodeEntries(final Properties properties) {
    for (String configuredNodeName : Properties.instance.seedNodes()) {
      final Id nodeId = Id.of(properties.nodeId(configuredNodeName));
      final Name nodeName = new Name(configuredNodeName);
      final Host host = Host.of(properties.host(configuredNodeName));
      final Address opNodeAddress = Address.from(host, properties.operationalPort(configuredNodeName), AddressType.OP);
      final Address appNodeAddress = Address.from(host, properties.applicationPort(configuredNodeName), AddressType.APP);

      nodes.add(new Node(nodeId, nodeName, opNodeAddress, appNodeAddress));
    }
  }
}

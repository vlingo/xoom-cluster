// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.metadata.MetadataCodec;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.node.LocalRegistry;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.List;
import java.util.stream.Collectors;

public class ClusterInitializer {
  private final ClusterCommunicationsHub communicationsHub;
  private final ClusterConfiguration configuration;
  private final Properties properties;
  private final Node localNode;
  private final Registry registry;

  ClusterInitializer(final String localNodeProperties, final Properties properties, final Logger logger) {
    this.configuration = new ClusterConfiguration(localNodeProperties, properties);
    this.properties = properties;
    this.localNode = configuration.localNode();
    this.communicationsHub = new ClusterCommunicationsHub(properties);
    this.registry = new LocalRegistry(logger, this.localNode, properties().clusterQuorum());
  }

  ClusterCommunicationsHub communicationsHub() {
    return this.communicationsHub;
  }

  ClusterConfiguration configuration() {
    return configuration;
  }

  Properties properties() {
    return properties;
  }

  Node localNode() {
    return localNode;
  }

  Id localNodeId() {
    return localNode.id();
  }

  Registry registry() {
    return registry;
  }

  ClusterConfig clusterConfig(MetadataCodec metadataCodec) {
    return clusterConfig(metadataCodec, localNode, configuration);
  }

  public static ClusterConfig clusterConfig(MetadataCodec metadataCodec, Node localNode, ClusterConfiguration configuration) {
    String localNodeHostName = localNode.operationalAddress().hostName();
    int localNodePort = localNode.operationalAddress().port();
    List<Address> seeds = configuration.seeds().stream()
            .map(seed -> Address.create(seed.operationalHost, seed.operationalPort))
            .collect(Collectors.toList());

    if (seeds.size() == 0) {
      throw new IllegalStateException("Must declare at least one node as seed in properties file.");
    }

    ClusterConfig config = new ClusterConfig()
            .memberAlias(localNode.id().valueString())
            .externalHost(localNodeHostName)
            .externalPort(localNodePort)
            .metadata(NodeProperties.from(localNode))
            .metadataCodec(metadataCodec)
            .transport(transportConfig -> transportConfig.port(localNodePort).transportFactory(new TcpTransportFactory()));

    if (!localNode.isSeed()) {
      config = config.membership(membershipConfig -> membershipConfig.seedMembers(seeds));
    }

    return config;
  }
}

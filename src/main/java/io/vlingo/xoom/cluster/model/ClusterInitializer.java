// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.node.LocalRegistry;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public class ClusterInitializer {
  private final ClusterCommunicationsHub communicationsHub;
  private final ClusterConfiguration configuration;
  private final Properties properties;
  private final Node localNode;
  private final Id localNodeId;
  private final Registry registry;

  ClusterInitializer(final String nodeNameText, final Properties properties, final Logger logger) {
    this.localNodeId = Id.of(properties.nodeId(nodeNameText));
    this.configuration = new ClusterConfiguration(properties, logger);
    this.properties = properties;
    this.localNode = configuration.nodeMatching(localNodeId);
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
    return localNodeId;
  }

  Registry registry() {
    return registry;
  }
}

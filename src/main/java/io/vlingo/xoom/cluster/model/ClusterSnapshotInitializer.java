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
import io.vlingo.xoom.wire.node.Configuration;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

class ClusterSnapshotInitializer {
  private final CommunicationsHub communicationsHub;
  private final Configuration configuration;
  private final Node localNode;
  private final Id localNodeId;
  private final Registry registry;

  ClusterSnapshotInitializer(final String nodeNameText, final Properties properties, final Logger logger) {
    this.localNodeId = Id.of(properties.nodeId(nodeNameText));

    this.configuration = new ClusterConfiguration(properties, logger);

    this.localNode = configuration.nodeMatching(localNodeId);

    this.communicationsHub = new NetworkCommunicationsHub(properties);

    this.registry = new LocalRegistry(this.localNode, this.configuration, logger);
  }

  CommunicationsHub communicationsHub() {
    return this.communicationsHub;
  }

  Configuration configuration() {
    return configuration;
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

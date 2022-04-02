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
    this.registry = new LocalRegistry(this.localNode, this.configuration, logger);
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

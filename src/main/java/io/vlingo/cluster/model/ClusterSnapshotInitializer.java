// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Logger;
import io.vlingo.cluster.model.node.LocalRegistry;
import io.vlingo.cluster.model.node.Registry;
import io.vlingo.wire.node.Configuration;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

class ClusterSnapshotInitializer {
  private final CommunicationsHub communicationsHub;
  private final Configuration configuration;
  private final Node localNode;
  private final Id localNodeId;
  private final Registry registry;
  
  ClusterSnapshotInitializer(final String nodeNameText, final Properties properties, final Logger logger) {
    this.localNodeId = Id.of(properties.nodeId(nodeNameText));
    
    this.configuration = new ClusterConfiguration(logger);
    
    this.localNode = configuration.nodeMatching(localNodeId);
    
    this.communicationsHub = new NetworkCommunicationsHub();
    
    this.registry = new LocalRegistry(this.localNode, this.configuration, logger);
  }

  protected CommunicationsHub communicationsHub() {
    return this.communicationsHub;
  }

  protected Configuration configuration() {
    return configuration;
  }

  protected Node localNode() {
    return localNode;
  }

  protected Id localNodeId() {
    return localNodeId;
  }
  
  protected Registry registry() {
    return registry;
  }
}

// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.*;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.wire.node.Address;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Host;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;

public class ClusterConfiguration {
  private final Logger logger;
  private final List<SeedNode> seeds;
  private final Node localNode;

  public ClusterConfiguration(String localNodeName, Properties properties, final Logger logger) {
    this.logger = logger;
    this.seeds = properties.seeds();

    final Id nodeId = Id.of(properties.nodeId(localNodeName));
    final Name nodeName = Name.of(localNodeName);
    final Host host = Host.of(properties.host(localNodeName));
    final Address opNodeAddress = Address.from(host, properties.operationalPort(localNodeName), AddressType.OP);
    final boolean isSeed = properties.isSeed(localNodeName);
    final Address appNodeAddress = Address.from(host, properties.applicationPort(localNodeName), AddressType.APP);

    this.localNode = new Node(nodeId, nodeName, opNodeAddress, appNodeAddress, isSeed);
  }

  public Node localNode() {
    return localNode;
  }

  public List<SeedNode> seeds() {
    return seeds;
  }

  public Logger logger() {
    return logger;
  }
}

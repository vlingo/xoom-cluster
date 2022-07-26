// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.*;

import io.vlingo.xoom.wire.node.Node;

public class ClusterConfiguration {
  private final List<SeedNode> seeds;
  private final Node localNode;

  public ClusterConfiguration(String localNodeProperties, Properties properties) {
    this.seeds = properties.seeds();

    this.localNode = NodeProperties.from(localNodeProperties)
            .asNode();
  }

  public Node localNode() {
    return localNode;
  }

  public List<SeedNode> seeds() {
    return seeds;
  }
}

// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import java.util.Set;

import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

/**
 * Registry with all live nodes from the cluster.
 */
public interface Registry {
  Set<Node> allOtherNodes();
  boolean containsNode(final Id id);
  Node getNode(final Id id);
  boolean isClusterHealthy();
  void join(Node node);
  void leave(final Id id);
  Set<Node> nodes();
  void startupIsCompleted();
}

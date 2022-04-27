// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public final class LocalRegistry implements Registry {
  private final Logger logger;
  private final Node localNode; // config info
  private final int quorum;

  private final Map<Id, Node> liveNodes;
  private final AtomicBoolean startupCompleted;

  public LocalRegistry(final Logger logger, final Node localNode, final int quorum) {
    this.logger = logger;
    this.localNode = localNode;
    this.quorum = quorum;

    this.liveNodes = new ConcurrentHashMap<>();
    this.startupCompleted = new AtomicBoolean(quorum == 1);
  }

  //======================================
  // Registry
  //======================================

  @Override
  public Set<Node> allOtherNodes() {
    return liveNodes.values().stream()
            .filter(n -> !localNode.id().equals(n.id()))
            .collect(Collectors.toSet());
  }

  @Override
  public boolean containsNode(final Id id) {
    return liveNodes.containsKey(id);
  }

  @Override
  public Node getNode(final Id id) {
    return liveNodes.get(id);
  }

  @Override
  public boolean isClusterHealthy() {
    return startupCompleted.get() && liveNodes.size() >= this.quorum;
  }

  @Override
  public void join(final Node node) {
    if (containsNode(node.id())) {
      logger.warn("Cannot join. Node '" + node.id() + "' is already in the cluster");
    } else {
      liveNodes.put(node.id(), node);
    }
  }

  @Override
  public void leave(final Id id) {
    Node status = liveNodes.remove(id);
    if (status == null) {
      logger.warn("Cannot leave because missing node: '" + id + "'");
    }
  }

  @Override
  public Set<Node> nodes() {
    return new HashSet<>(liveNodes.values());
  }

  @Override
  public void startupIsCompleted() {
    startupCompleted.set(true);
  }
}

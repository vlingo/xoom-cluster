// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.wire.node.Configuration;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public final class LocalRegistry implements Registry {
  private final RegistryInterestBroadcaster broadcaster;
  private final Configuration configuration;
  private final Node localNode;
  private final Logger logger;
  private Map<Id, RegisteredNodeStatus> registry;

  public LocalRegistry(final Node localNode, final Configuration confirguration, final Logger logger) {
    this.localNode = localNode;
    this.configuration = confirguration;
    this.logger = logger;
    this.broadcaster = new RegistryInterestBroadcaster(logger);
    this.registry = new TreeMap<>();
  }

  //======================================
  // Registry
  //======================================

  @Override
  public void cleanTimedOutNodes() {
    final long currentTime = System.currentTimeMillis();
    final long liveNodeTimeout = Properties.instance().clusterLiveNodeTimeout();

    final Map<Id, RegisteredNodeStatus> nodesToKeep = new TreeMap<>();

    for (final RegisteredNodeStatus status : registry.values()) {
      if (!status.isTimedOut(currentTime, liveNodeTimeout)) {
        nodesToKeep.put(status.node().id(), status);
      } else {
        demoteLeaderOf(status.node().id());
        broadcaster.informNodeTimedOut(status.node(), isClusterHealthy());
        logger.info("Node cleaned from registry due to timeout: " + status.node());
      }
    }

    registry = nodesToKeep;
  }

  @Override
  public void confirmAllLiveNodesByLeader() {
    for (final RegisteredNodeStatus status : registry.values()) {
      status.confirmedByLeader(true);
      broadcaster.informConfirmedByLeader(status.node(), isClusterHealthy());
    }
  }

  @Override
  public boolean isConfirmedByLeader(final Id id) {
    final RegisteredNodeStatus status = registry.get(id);

    if (status != null) {
      return status.isConfirmedByLeader();
    }
    return false;
  }

  @Override
  public Node currentLeader() {
    for (final RegisteredNodeStatus status : registry.values()) {
      if (status.isLeader()) {
        return status.node();
      }
    }
    return Node.NO_NODE;
  }

  @Override
  public void declareLeaderAs(final Id id) {
    final RegisteredNodeStatus status = registry.get(id);

    if (status != null) {
      status.lead(true);
      status.updateLastHealthIndication();
      broadcaster.informCurrentLeader(status.node(), isClusterHealthy());
      demotePreviousLeader(id);
    } else {
      logger.warn("Cannot declare leader because missing node: '" + id + "'");
    }
  }

  @Override
  public void demoteLeaderOf(final Id id) {
    final RegisteredNodeStatus status = registry.get(id);

    if (status != null && status.isLeader()) {
      status.lead(false);
      broadcaster.informLeaderDemoted(status.node(), isClusterHealthy());
    }
  }

  @Override
  public boolean isLeader(final Id id) {
    final RegisteredNodeStatus status = registry.get(id);

    if (status != null) {
      return status.isLeader();
    }

    return false;
  }

  @Override
  public boolean hasLeader() {
    for (final RegisteredNodeStatus status : registry.values()) {
      if (status.isLeader()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Set<Node> liveNodes() {
    final Set<Node> liveNodes = new TreeSet<>();
    for (RegisteredNodeStatus status : registry.values()) {
      liveNodes.add(status.node());
    }
    return liveNodes;
  }

  @Override
  public boolean hasMember(final Id id) {
    return registry.containsKey(id);
  }

  @Override
  public boolean hasQuorum() {
    final int quorum = (configuration.totalNodes() / 2) + 1;

    return liveNodes().size() >= quorum;
  }

  @Override
  public void join(final Node node) {
    if (!hasMember(node.id())) {
      registry.put(node.id(), new RegisteredNodeStatus(node, false, false));
      broadcaster.informNodeJoinedCluster(node, isClusterHealthy());
      broadcaster.informAllLiveNodes(liveNodes(), isClusterHealthy());
    }
  }

  @Override
  public void leave(final Id id) {
    RegisteredNodeStatus status = registry.remove(id);
    if (status != null) {
      demoteLeaderOf(id);
      broadcaster.informNodeLeftCluster(status.node(), isClusterHealthy());
      broadcaster.informAllLiveNodes(liveNodes(), isClusterHealthy());
    } else {
      logger.warn("Cannot leave because missing node: '" + id + "'");
    }
  }

  @Override
  public void mergeAllDirectoryEntries(final Collection<Node> leaderRegisteredNodes) {
    final Set<MergeResult> result = new TreeSet<>();
    final Map<Id, RegisteredNodeStatus> mergedNodes = new TreeMap<>();

    for (final Node node : leaderRegisteredNodes) {
      mergedNodes.put(node.id(), new RegisteredNodeStatus(node, isLeader(node.id()), true));
    }

    for (final RegisteredNodeStatus status : mergedNodes.values()) {
      if (!registry.containsKey(status.node().id())) {
        result.add(new MergeResult(status.node(), true));
      }
    }

    for (final RegisteredNodeStatus status : registry.values()) {
      if (!mergedNodes.containsKey(status.node().id())) {
        demoteLeaderOf(status.node().id());
        result.add(new MergeResult(status.node(), false));
      }
    }

    registry = mergedNodes;

    broadcaster.informMergedAllDirectoryEntries(liveNodes(), result, isClusterHealthy());
    broadcaster.informAllLiveNodes(liveNodes(), isClusterHealthy());
  }

  @Override
  public void promoteElectedLeader(Id leaderNodeId) {
    if (localNode.id().equals(leaderNodeId)) {

      declareLeaderAs(leaderNodeId);

      confirmAllLiveNodesByLeader();

    } else {

      if (isLeader(localNode.id())) {
        demoteLeaderOf(localNode.id());
      }

      if (!hasMember(leaderNodeId)) {
        join(configuration.nodeMatching(leaderNodeId));
      }

      declareLeaderAs(leaderNodeId);
    }

    broadcaster.informCurrentLeader(registry.get(leaderNodeId).node(), isClusterHealthy());
  }

  @Override
  public void registerRegistryInterest(final RegistryInterest interest) {
    broadcaster.registerRegistryInterest(interest);
  }

  @Override
  public boolean isSingleNodeCluster() {
    return configuration.totalNodes() == 1;
  }

  @Override
  public void updateLastHealthIndication(final Id id) {
    final RegisteredNodeStatus status = registry.get(id);

    if (status != null) {
      status.updateLastHealthIndication();
      broadcaster.informNodeIsHealthy(status.node(), isClusterHealthy());
    }
  }

  RegisteredNodeStatus registeredNodeStatusOf(final Id id) {
    return registry.get(id);
  }

  private boolean isClusterHealthy() {
    return hasQuorum() && hasLeader();
  }

  private void demotePreviousLeader(final Id currentLeaderNodeId) {
    for (final RegisteredNodeStatus status : registry.values()) {
      if (!status.node().id().equals(currentLeaderNodeId) && status.isLeader()) {
        status.lead(false);
        broadcaster.informLeaderDemoted(status.node(), isClusterHealthy());
      }
    }
  }
}

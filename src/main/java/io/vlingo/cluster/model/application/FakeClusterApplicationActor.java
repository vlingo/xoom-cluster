// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application;

import java.util.Collection;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.common.message.RawMessage;

public class FakeClusterApplicationActor extends ClusterApplicationAdapter {

  private final Node localNode;
  
  public FakeClusterApplicationActor(final Node localNode) {
    this.localNode = localNode;
  }

  @Override
  public boolean isStarted() {
    return !isStopped();
  }

  @Override
  public void start() {
    System.out.println("APP: ClusterApplication started on node: " + localNode);
  }

  @Override
  public void handleApplicationMessage(final RawMessage message, final ClusterApplicationOutboundStream responder) {
    System.out.println("APP: Received application message: " + message.asTextMessage());
  }

  @Override
  public void informAllLiveNodes(final Collection<Id> liveNodes, final boolean isHealthyCluster) {
    for (final Id id : liveNodes) {
      System.out.println("APP: Live node confirmed: " + id);
    }
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading) {
    System.out.println("APP: Leader elected: " + leaderId);
    printHealthy(isHealthyCluster);
    if (isLocalNodeLeading) {
      System.out.println("APP: Local node is leading.");
    }
  }

  @Override
  public void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster) {
    System.out.println("APP: Leader lost: " + lostLeaderId);
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informLocalNodeShutDown(final Id nodeId) {
    System.out.println("APP: Local node shut down: " + nodeId);
  }

  @Override
  public void informLocalNodeStarted(final Id nodeId) {
    System.out.println("APP: Local node started: " + nodeId);
  }

  @Override
  public void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster) {
    System.out.println("APP: Node reported healthy: " + nodeId);
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
    System.out.println("APP: " + nodeId + " joined cluster");
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
    System.out.println("APP: " + nodeId + " left cluster");
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informQuorumAchieved() {
    System.out.println("APP: Quorum achieved");
    printHealthy(true);
  }

  @Override
  public void informQuorumLost() {
    System.out.println("APP: Quorum lost");
    printHealthy(false);
  }

  private void printHealthy(final boolean isHealthyCluster) {
    if (isHealthyCluster) {
      System.out.println("APP: Cluster is healthy");
    } else {
      System.out.println("APP: Cluster is NOT healthy");
    }
  }
}

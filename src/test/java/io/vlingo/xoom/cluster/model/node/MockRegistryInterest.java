// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import java.util.Collection;

import io.vlingo.xoom.wire.node.Node;

public class MockRegistryInterest implements RegistryInterest {
  public int informAllLiveNodes;
  public int informConfirmedByLeader;
  public int informCurrentLeader;
  public int informMergedAllDirectoryEntries;
  public int informLeaderDemoted;
  public int informNodeIsHealthy;
  public int informNodeJoinedCluster;
  public int informNodeLeftCluster;
  public int informNodeTimedOut;
  
  public Collection<Node> liveNodes;
  public Collection<MergeResult> mergeResults;
  
  @Override
  public void informAllLiveNodes(Collection<Node> liveNodes, boolean isHealthyCluster) {
    ++informAllLiveNodes;
  }

  @Override
  public void informConfirmedByLeader(Node node, boolean isHealthyCluster) {
    ++informConfirmedByLeader;
  }

  @Override
  public void informCurrentLeader(Node node, boolean isHealthyCluster) {
    ++informCurrentLeader;
  }

  @Override
  public void informMergedAllDirectoryEntries(
          Collection<Node> liveNodes,
          Collection<MergeResult> mergeResults,
          boolean isHealthyCluster) {
    
    this.liveNodes = liveNodes;
    this.mergeResults = mergeResults;
    
    ++informMergedAllDirectoryEntries;
  }

  @Override
  public void informLeaderDemoted(Node node, boolean isHealthyCluster) {
    ++informLeaderDemoted;
  }

  @Override
  public void informNodeIsHealthy(Node node, boolean isHealthyCluster) {
    ++informNodeIsHealthy;
  }

  @Override
  public void informNodeJoinedCluster(Node node, boolean isHealthyCluster) {
    ++informNodeJoinedCluster;
  }

  @Override
  public void informNodeLeftCluster(Node node, boolean isHealthyCluster) {
    ++informNodeLeftCluster;
  }

  @Override
  public void informNodeTimedOut(Node node, boolean isHealthyCluster) {
    ++informNodeTimedOut;
  }
}

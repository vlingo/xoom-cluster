// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.Collection;

import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.application.ClusterApplicationOutboundStream;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.common.message.RawMessage;

public class MockClusterApplication implements ClusterApplication {
  public int allLiveNodes;
  public int handleApplicationMessage;
  public int informLeaderElected;
  public int informLeaderLost;
  public int informLocalNodeShutDown;
  public int informLocalNodeStarted;
  public int informNodeIsHealthy;
  public int informNodeJoinedCluster;
  public int informNodeLeftCluster;
  public int informQuorumAchieved;
  public int informQuorumLost;
  public int stop;
  
  @Override
  public boolean isStarted() {
    return true;
  }

  @Override
  public void start() {
    
  }

  @Override
  public boolean isStopped() {
    return false;
  }

  @Override
  public void stop() {
    ++stop;
  }

  @Override
  public void handleApplicationMessage(RawMessage message, ClusterApplicationOutboundStream responder) {
    ++handleApplicationMessage;
  }

  @Override
  public void informAllLiveNodes(Collection<Id> liveNodes, boolean isHealthyCluster) {
    ++allLiveNodes;
  }

  @Override
  public void informLeaderElected(Id leaderId, boolean isHealthyCluster, boolean isLocalNodeLeading) {
    ++informLeaderElected;
  }

  @Override
  public void informLeaderLost(Id lostLeaderId, boolean isHealthyCluster) {
    ++informLeaderLost;
  }

  @Override
  public void informLocalNodeShutDown(Id nodeId) {
    ++informLocalNodeShutDown;
  }

  @Override
  public void informLocalNodeStarted(Id nodeId) {
    ++informLocalNodeStarted;
  }

  @Override
  public void informNodeIsHealthy(Id nodeId, boolean isHealthyCluster) {
    ++informNodeIsHealthy;
  }

  @Override
  public void informNodeJoinedCluster(Id nodeId, boolean isHealthyCluster) {
    ++informNodeJoinedCluster;
  }

  @Override
  public void informNodeLeftCluster(Id nodeId, boolean isHealthyCluster) {
    ++informNodeLeftCluster;
  }

  @Override
  public void informQuorumAchieved() {
    ++informQuorumAchieved;
  }

  @Override
  public void informQuorumLost() {
    ++informQuorumLost;
  }
}

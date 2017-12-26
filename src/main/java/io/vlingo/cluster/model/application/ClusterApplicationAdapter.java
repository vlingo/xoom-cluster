// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application;

import java.util.Collection;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.common.message.RawMessage;

public abstract class ClusterApplicationAdapter extends ClusterApplicationActor implements ClusterApplication {

  public ClusterApplicationAdapter() { }

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
  }

  @Override
  public void handleApplicationMessage(final RawMessage message, final ClusterApplicationOutboundStream responder) {
  }

  @Override
  public void informAllLiveNodes(final Collection<Id> liveNodes, final boolean isHealthyCluster) {
  }

  @Override
  public void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading) {
  }

  @Override
  public void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster) {
  }

  @Override
  public void informLocalNodeShutDown(final Id nodeId) {
  }

  @Override
  public void informLocalNodeStarted(final Id nodeId) {
  }

  @Override
  public void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster) {
  }

  @Override
  public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
  }

  @Override
  public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
  }

  @Override
  public void informQuorumAchieved() {
  }

  @Override
  public void informQuorumLost() {
  }
}

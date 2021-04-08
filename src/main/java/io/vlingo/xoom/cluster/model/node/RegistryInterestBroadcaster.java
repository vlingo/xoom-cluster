// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.wire.node.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class RegistryInterestBroadcaster implements RegistryInterest {
  private final Logger logger;
  private final List<RegistryInterest> registryInterests;
  
  RegistryInterestBroadcaster(final Logger logger) {
    this.logger = logger;
    this.registryInterests = new ArrayList<>();
  }

  public void registerRegistryInterest(final RegistryInterest interest) {
    registryInterests.add(interest);
  }

  //========================================
  // RegistryInterest
  //========================================
  
  @Override
  public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informAllLiveNodes(liveNodes, isHealthyCluster));
  }

  @Override
  public void informConfirmedByLeader(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informConfirmedByLeader(node, isHealthyCluster));
  }

  @Override
  public void informCurrentLeader(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informCurrentLeader(node, isHealthyCluster));
  }

  @Override
  public void informMergedAllDirectoryEntries(final Collection<Node> liveNodes, Collection<MergeResult> mergeResults, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informMergedAllDirectoryEntries(liveNodes, mergeResults, isHealthyCluster));
  }

  @Override
  public void informLeaderDemoted(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informLeaderDemoted(node, isHealthyCluster));
  }

  @Override
  public void informNodeIsHealthy(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informNodeIsHealthy(node, isHealthyCluster));
  }

  @Override
  public void informNodeJoinedCluster(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informNodeJoinedCluster(node, isHealthyCluster));
  }

  @Override
  public void informNodeLeftCluster(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informNodeLeftCluster(node, isHealthyCluster));
  }

  @Override
  public void informNodeTimedOut(final Node node, final boolean isHealthyCluster) {
    broadcast((interest) -> interest.informNodeTimedOut(node, isHealthyCluster));
  }
  
  private void broadcast(final Consumer<RegistryInterest> inform) {
    for (final RegistryInterest interest : registryInterests) {
      try {
        inform.accept(interest);
      } catch (Exception e) {
        logger.error("Cannot inform because: " + e.getMessage(), e);
      }
    }
  }
}

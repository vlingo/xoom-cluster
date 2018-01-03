// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegistryInterestBroadcaster implements RegistryInterest {
  private final List<RegistryInterest> registryInterests;
  
  RegistryInterestBroadcaster() {
    this.registryInterests = new ArrayList<RegistryInterest>();
  }

  public void registerRegistryInterest(final RegistryInterest interest) {
    registryInterests.add(interest);
  }

  //========================================
  // RegistryInterest
  //========================================
  
  @Override
  public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informAllLiveNodes(liveNodes, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informConfirmedByLeader(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informConfirmedByLeader(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informCurrentLeader(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informCurrentLeader(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informMergedAllDirectoryEntries(final Collection<Node> liveNodes, Collection<MergeResult> mergeResults, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informMergedAllDirectoryEntries(liveNodes, mergeResults, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informLeaderDemoted(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informLeaderDemoted(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeIsHealthy(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informNodeIsHealthy(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeJoinedCluster(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informNodeJoinedCluster(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeLeftCluster(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informNodeLeftCluster(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeTimedOut(final Node node, final boolean isHealthyCluster) {
    try {
      for (final RegistryInterest interest : registryInterests) {
        interest.informNodeTimedOut(node, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }
}

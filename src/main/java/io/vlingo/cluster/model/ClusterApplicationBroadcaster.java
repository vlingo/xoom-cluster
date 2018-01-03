// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.application.ClusterApplicationOutboundStream;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.common.message.RawMessage;

class ClusterApplicationBroadcaster implements ClusterApplication {
  private List<ClusterApplication> clusterApplications;

  ClusterApplicationBroadcaster() {
    this.clusterApplications = new ArrayList<ClusterApplication>();
  }

  public void registerClusterApplication(final ClusterApplication clusterApplication) {
    clusterApplications.add(clusterApplication);
  }

  //========================================
  // ClusterApplication
  //========================================

  @Override
  public void informAllLiveNodes(final Collection<Id> liveNodes, final boolean isHealthyCluster) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informAllLiveNodes(liveNodes, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informLeaderElected(leaderId, isHealthyCluster, isLocalNodeLeading);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informLeaderLost(lostLeaderId, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informLocalNodeShutDown(final Id nodeId) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informLocalNodeShutDown(nodeId);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informLocalNodeStarted(final Id nodeId) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informLocalNodeStarted(nodeId);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informNodeIsHealthy(nodeId, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informNodeJoinedCluster(nodeId, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informNodeLeftCluster(nodeId, isHealthyCluster);
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informQuorumAchieved() {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informQuorumAchieved();
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public void informQuorumLost() {
    try {
      for (final ClusterApplication app : clusterApplications) {
        app.informQuorumLost();
      }
    } catch (Exception e) {
      // TODO: Log
    }
  }

  @Override
  public boolean isStarted() {
    return false;
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
  public void handleApplicationMessage(RawMessage message, ClusterApplicationOutboundStream responder) {
  }
}

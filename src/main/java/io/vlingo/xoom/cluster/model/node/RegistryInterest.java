// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import java.util.Collection;

import io.vlingo.xoom.wire.node.Node;

public interface RegistryInterest {
  void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster);
  void informConfirmedByLeader(final Node node, final boolean isHealthyCluster);
  void informCurrentLeader(final Node node, final boolean isHealthyCluster);
  void informMergedAllDirectoryEntries(final Collection<Node> liveNodes, final Collection<MergeResult> mergeResults, final boolean isHealthyCluster);
  void informLeaderDemoted(final Node node, final boolean isHealthyCluster);
  void informNodeIsHealthy(final Node node, final boolean isHealthyCluster);
  void informNodeJoinedCluster(final Node node, final boolean isHealthyCluster);
  void informNodeLeftCluster(final Node node, final boolean isHealthyCluster);
  void informNodeTimedOut(final Node node, final boolean isHealthyCluster);
}

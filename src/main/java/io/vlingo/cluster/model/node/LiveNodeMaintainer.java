// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import java.util.Set;

import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;
import io.vlingo.wire.node.NodeSynchronizer;

interface LiveNodeMaintainer extends NodeSynchronizer {
  void assertNewLeadership(final Id id);
  void declareLeadership();
  void declareNodeSplit(final Id leaderNodeId);
  void dropNode(final Id id);
  void escalateElection(final Id id);
  void join(final Node node);
  void joinLocalWith(final Node remoteNode);
  void mergeAllDirectoryEntries(final Set<Node> nodes);
  void overtakeLeadership(final Id leaderNodeId);
  void placeVote(final Id voterId);
  void providePulseTo(final Id id);
  void updateLastHealthIndication(final Id id);
  void voteForLocalNode(final Id targetNodeId);
}

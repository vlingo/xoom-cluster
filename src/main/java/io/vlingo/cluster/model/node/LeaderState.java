// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import io.vlingo.actors.Logger;
import io.vlingo.cluster.model.message.Directory;
import io.vlingo.cluster.model.message.Elect;
import io.vlingo.cluster.model.message.Join;
import io.vlingo.cluster.model.message.Leader;
import io.vlingo.cluster.model.message.Leave;
import io.vlingo.cluster.model.message.Vote;
import io.vlingo.wire.node.Node;

final class LeaderState extends LiveNodeState {
  LeaderState(final Node node, final LiveNodeMaintainer liveNodeMaintainer, final Logger logger) {
    super(node, liveNodeMaintainer, Type.LEADER, logger);
  }

  @Override
  protected void handle(final Directory dir) {
    logger.log("" + type + " " + node.id() + " DIRECTORY: " + dir);
    
    if (dir.id().greaterThan(node.id())) {
      
      // apparently a new bully is taking leadership --
      // perhaps there was a race for leadership on newly
      // joined node with higher nodeId
      
      liveNodeMaintainer.mergeAllDirectoryEntries(dir.nodes());
      
    } else {
      logger.log("Leader must not receive Directory message from follower: '" + dir.id() + "'");
    }
  }

  @Override
  protected void handle(final Elect elect) {
    logger.log("" + type + " " + node.id() + " ELECT: " + elect);
    liveNodeMaintainer.voteForLocalNode(elect.id());
  }

  @Override
  protected void handle(final Join join) {
    logger.log("" + type + " " + node.id() + " JOIN: " + join);
    liveNodeMaintainer.join(join.node());
  }

  @Override
  protected void handle(final Leader leader) {
    logger.log("" + type + " " + node.id() + " LEADER: " + leader);
    if (leader.id().equals(node.id())) {
      logger.log("Leader must not receive Leader message of itself from a follower.");
    } else if (leader.id().greaterThan(node.id())) {
      liveNodeMaintainer.overtakeLeadership(leader.id());
    } else {
      liveNodeMaintainer.declareLeadership();
    }
  }

  @Override
  protected void handle(final Leave leave) {
    logger.log("" + type + " " + node.id() + " LEAVE: " + leave);
    if (leave.id().equals(node.id())) {
      logger.log("Leader must not receive Leave message of itself from a follower.");
    } else {
      liveNodeMaintainer.dropNode(leave.id());
    }
  }

  @Override
  protected void handle(final Vote vote) {
    logger.log("" + type + " " + node.id() + " VOTE: " + vote);
    liveNodeMaintainer.declareLeadership();
  }
}

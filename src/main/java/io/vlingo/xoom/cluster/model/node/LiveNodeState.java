// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.cluster.model.message.Directory;
import io.vlingo.xoom.cluster.model.message.Elect;
import io.vlingo.xoom.cluster.model.message.Join;
import io.vlingo.xoom.cluster.model.message.Leader;
import io.vlingo.xoom.cluster.model.message.Leave;
import io.vlingo.xoom.cluster.model.message.Ping;
import io.vlingo.xoom.cluster.model.message.Pulse;
import io.vlingo.xoom.cluster.model.message.Split;
import io.vlingo.xoom.cluster.model.message.Vote;
import io.vlingo.xoom.wire.node.Node;

abstract class LiveNodeState {
  final LiveNodeMaintainer liveNodeMaintainer;
  final Logger logger;
  final Node node;
  final Type type;

  final TimeoutTracker noQuorumTracker =
      new TimeoutTracker(Properties.instance().clusterQuorumTimeout());

  final TimeoutTracker leaderElectionTracker =
      new TimeoutTracker(Properties.instance().clusterHeartbeatInterval());

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[type=" + type + " node=" + node + "]";
  }

  LiveNodeState(final Node node, final LiveNodeMaintainer liveNodeMaintainer, final Type type, final Logger logger) {
    this.node = node;
    this.liveNodeMaintainer = liveNodeMaintainer;
    this.type = type;
    this.logger = logger;
  }

  protected void handle(final Directory dir) {
    logger.debug("" + type + " " + node.id() + " DIRECTORY: " + dir);
    liveNodeMaintainer.mergeAllDirectoryEntries(dir.nodes());
  }

  protected void handle(final Elect elect) {
    logger.debug("" + type + " " + node.id() + " ELECT: " + elect);
    liveNodeMaintainer.escalateElection(elect.id());
  }

  protected void handle(final Join join) {
    logger.debug("" + type + " " + node.id() + " JOIN: " + join);
    liveNodeMaintainer.joinLocalWith(join.node());
  }

  protected void handle(final Leader leader) {
    logger.debug("" + type + " " + node.id() + " LEADER: " + leader);
    liveNodeMaintainer.assertNewLeadership(leader.id());
  }

  protected void handle(final Leave leave) {
    logger.debug("" + type + " " + node.id() + " LEAVE: " + leave);
    liveNodeMaintainer.dropNode(leave.id());
  }

  protected void handle(final Ping ping) {
    logger.debug("" + type + " " + node.id() + " PING: " + ping);
    liveNodeMaintainer.providePulseTo(ping.id());
  }

  protected void handle(final Pulse pulse) {
    // logger.debug("" + type + " " + node.id() + " PULSE: " + pulse);
    liveNodeMaintainer.updateLastHealthIndication(pulse.id());
  }

  protected void handle(final Split split) {
    logger.debug("" + type + " " + node.id() + " SPLIT: " + split);
    liveNodeMaintainer.declareNodeSplit(split.id());
  }

  protected void handle(final Vote vote) {
    logger.debug("" + type + " " + node.id() + " VOTE: " + vote);
    liveNodeMaintainer.placeVote(node.id());
  }

  protected boolean isIdle() {
    return type == Type.IDLE;
  }

  protected boolean isFollower() {
    return type == Type.FOLLOWER;
  }

  protected boolean isLeader() {
    return type == Type.LEADER;
  }

  enum Type {
    IDLE, FOLLOWER, LEADER
  }
}

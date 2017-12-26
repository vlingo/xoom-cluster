// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import io.vlingo.cluster.model.Properties;
import io.vlingo.cluster.model.message.*;

abstract class LiveNodeState {
  protected final LiveNodeMaintainer liveNodeMaintainer;
  protected final Node node;
  protected final Type type;

  protected final TimeoutTracker noQuorumTracker =
      new TimeoutTracker(Properties.instance.clusterQuorumTimeout());

  protected final TimeoutTracker leaderElectionTracker =
      new TimeoutTracker(Properties.instance.clusterHeartbeatInterval());

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[type=" + type + " node=" + node + "]";
  }

  protected LiveNodeState(final Node node, final LiveNodeMaintainer liveNodeMaintainer, final Type type) {
    this.node = node;
    this.liveNodeMaintainer = liveNodeMaintainer;
    this.type = type;
  }

  protected void handle(final Directory dir) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " DIRECTORY: " + dir);
    liveNodeMaintainer.mergeAllDirectoryEntries(dir.nodes());
  }

  protected void handle(final Elect elect) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " ELECT: " + elect);
    liveNodeMaintainer.escalateElection(elect.id());
  }

  protected void handle(final Join join) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " JOIN: " + join);
    liveNodeMaintainer.joinLocalWith(join.node());
  }

  protected void handle(final Leader leader) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " LEADER: " + leader);
    liveNodeMaintainer.assertNewLeadership(leader.id());
  }
  
  protected void handle(final Leave leave) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " LEAVE: " + leave);
    liveNodeMaintainer.dropNode(leave.id());
  }

  protected void handle(final Ping ping) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " PING: " + ping);
    liveNodeMaintainer.providePulseTo(ping.id());
  }

  protected void handle(final Pulse pulse) {
    // System.out.println("vlingo/cluster: " + type + " " + node.id() + " PULSE: " + pulse);
    liveNodeMaintainer.updateLastHealthIndication(pulse.id());
  }

  protected void handle(final Split split) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " SPLIT: " + split);
    liveNodeMaintainer.declareNodeSplit(split.id());
  }

  protected void handle(final Vote vote) {
    System.out.println("vlingo/cluster: " + type + " " + node.id() + " VOTE: " + vote);
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

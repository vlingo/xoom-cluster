// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import io.vlingo.xoom.wire.node.Node;

public class RegisteredNodeStatus {
  private boolean confirmedByLeader;
  private long lastHealthIndication;
  private boolean leader;
  private final Node node;

  public void confirmedByLeader(final boolean isConfirmed) {
    this.confirmedByLeader = isConfirmed;
  }

  public boolean isConfirmedByLeader() {
    return confirmedByLeader;
  }

  public boolean isLeader() {
    return leader;
  }

  public boolean isTimedOut(final long currentTime, final long liveNodeTimeout) {
    final long timeOutTime = lastHealthIndication() + liveNodeTimeout;
    return timeOutTime < currentTime;
  }

  public long lastHealthIndication() {
    return lastHealthIndication;
  }

  public void lead(final boolean lead) {
    this.leader = lead;
  }

  public Node node() {
    return node;
  }

  public void updateLastHealthIndication() {
    this.lastHealthIndication = System.currentTimeMillis();
  }

  RegisteredNodeStatus(final Node node, final boolean isLeader, final boolean confirmedByLeader) {
    this.node = node;
    this.leader = isLeader;
    this.lastHealthIndication = System.currentTimeMillis();
    this.confirmedByLeader = confirmedByLeader;
  }
  
  void setLastHealthIndication(final long millis) {
    this.lastHealthIndication = millis;
  }
}

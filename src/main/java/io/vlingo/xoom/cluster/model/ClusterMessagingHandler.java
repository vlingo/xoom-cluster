// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public class ClusterMessagingHandler implements ClusterMessageHandler {
  private final Logger logger;
  private final ClusterMembershipControl membershipControl;
  private final ClusterConfiguration configuration;
  private final Properties properties;

  public ClusterMessagingHandler(Logger logger, ClusterMembershipControl membershipControl, ClusterConfiguration configuration, Properties properties) {
    this.logger = logger;
    this.membershipControl = membershipControl;
    this.configuration = configuration;
    this.properties = properties;
  }

  @Override
  public void onMembershipEvent(MembershipEvent event) {
    logger.debug("Received cluster membership event: " + event);
    String nodeName = event.member().alias();

    if (nodeName != null && nodeName.startsWith("seed")) {
      if (event.isAdded()) {
        membershipControl.seedAdded(nodeName);
      } else if (event.isRemoved()) {
        membershipControl.seedRemoved(nodeName);
      } else if (event.isLeaving()) {
        membershipControl.seedLeaving(nodeName);
      } else {
        logger.warn("Event type: " + event.type() + " is not handled for now. Received from cluster member: " + event.member());
      }
    } else {
      Node sender = configuration.nodeMatching(Id.of(properties.nodeId(nodeName)));
      if (event.isAdded()) {
        membershipControl.nodeAdded(sender);
      } else if (event.isRemoved()) {
        membershipControl.nodeRemoved(sender);
      } else if (event.isLeaving()) {
        membershipControl.nodeLeaving(sender);
      } else {
        logger.warn("Event type: " + event.type() + " is not handled for now. Received from cluster member: " + event.member());
      }
    }
  }
}

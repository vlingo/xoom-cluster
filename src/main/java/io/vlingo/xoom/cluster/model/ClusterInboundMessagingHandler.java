// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.message.RawMessageBuilder;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.nio.ByteBuffer;

public class ClusterInboundMessagingHandler implements ClusterMessageHandler {
  private final Logger logger;
  private final ClusterMembershipControl membershipControl;
  private final InboundStreamInterest inboundInterest;
  private final ClusterConfiguration configuration;
  private final Properties properties;

  public ClusterInboundMessagingHandler(Logger logger, ClusterMembershipControl membershipControl, InboundStreamInterest inboundInterest, ClusterConfiguration configuration, Properties properties) {
    this.logger = logger;
    this.membershipControl = membershipControl;
    this.inboundInterest = inboundInterest;
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

  @Override
  public void onGossip(Message gossip) {
    logger.debug("Received cluster gossip: " + gossip);
    dispatch(gossip);
  }

  @Override
  public void onMessage(Message message) {
    logger.debug("Received cluster message: " + message);
    dispatch(message);
  }

  private void dispatch(Message message) {
    final RawMessageBuilder builder = new RawMessageBuilder(properties.applicationBufferSize());

    builder.workBuffer().put(ByteBuffer.wrap(message.data()));
    if (!builder.hasContent()) {
      return;
    }

    builder.prepareContent().sync();
    try {
      final RawMessage rawMessage = builder.currentRawMessage();
      inboundInterest.handleInboundStreamMessage(AddressType.OP, rawMessage);
    } catch (Exception e) {
      logger.error("Failed to dispatch message!", e);
    }
  }
}

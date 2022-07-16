// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.metadata.MetadataCodec;
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
  private final MetadataCodec metadataCodec;
  private final ClusterMembershipControl membershipControl;
  private final InboundStreamInterest inboundInterest;
  private final Properties properties;

  public ClusterInboundMessagingHandler(Logger logger, MetadataCodec metadataCodec, ClusterMembershipControl membershipControl, InboundStreamInterest inboundInterest, Properties properties) {
    this.logger = logger;
    this.metadataCodec = metadataCodec;
    this.membershipControl = membershipControl;
    this.inboundInterest = inboundInterest;
    this.properties = properties;
  }

  @Override
  public void onMembershipEvent(MembershipEvent event) {
    logger.debug("Received cluster membership event: " + event);
    Id nodeId = Id.of(event.member().alias());

    if (event.isAdded()) {
      NodeMetadata nodeMetadata = (NodeMetadata) metadataCodec.deserialize(event.newMetadata());
      Node sender = nodeMetadata.asNode();
      membershipControl.nodeAdded(sender);
    } else if (event.isRemoved()) {
      membershipControl.nodeRemoved(nodeId);
    } else if (event.isLeaving()) {
      membershipControl.nodeLeaving(nodeId);
    } else {
      logger.warn("Event type: " + event.type() + " is not handled for now. Received from cluster member: " + event.member());
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

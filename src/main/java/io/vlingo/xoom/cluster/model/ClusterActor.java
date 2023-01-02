// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.codec.jackson.smile.JacksonSmileMetadataCodec;
import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.attribute.AttributesAgent;
import io.vlingo.xoom.cluster.model.message.OperationalMessage;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.common.Scheduled;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Node;

public class ClusterActor extends Actor implements ClusterControl, InboundStreamInterest, Scheduled<Object> {

  private final Registry registry;
  private final AttributesAgent attributesAgent; // null when single node
  private final ClusterApplication clusterApplication; // only one application for now
  private final ClusterImpl cluster; // null when single node
  private final ClusterMembershipControl membershipControl; // null when single node
  private final ClusterCommunicationsHub communicationsHub;

  public ClusterActor(final ClusterInitializer initializer, final ClusterApplication clusterApplication) throws Exception {
    this.registry = initializer.registry();
    this.clusterApplication = clusterApplication;

    final Node localNode = initializer.localNode();
    this.communicationsHub = initializer.communicationsHub();
    this.communicationsHub.openAppChannel(stage(), initializer.localNode(), selfAs(InboundStreamInterest.class));

    this.clusterApplication.start();
    this.clusterApplication.informResponder(communicationsHub.applicationOutboundStream());

    if (initializer.properties().singleNode()) {
      this.membershipControl = null;
      this.cluster = null;
      this.attributesAgent = null;
      initializer.registry().join(localNode);
      intervalSignal(null, null);
    } else {
      final JacksonSmileMetadataCodec metadataCodec = new JacksonSmileMetadataCodec();
      final ClusterConfig config = initializer.clusterConfig(metadataCodec);
      this.membershipControl = new ClusterMembershipControl(clusterApplication,
              new OutboundChannelInterest(communicationsHub.outboundChannelProvider()),
              initializer);
      this.cluster = new ClusterImpl(config)
              .handler(c -> new ClusterInboundMessagingHandler(logger(),
                      metadataCodec,
                      membershipControl,
                      selfAs(InboundStreamInterest.class),
                      initializer.properties()));

      registry.join(localNode);
      this.communicationsHub.openOpChannel(stage(), registry, cluster);

      this.attributesAgent = AttributesAgent.instance(stage(),
              localNode,
              clusterApplication,
              communicationsHub.operationalOutboundStream(),
              registry,
              logger());

      this.cluster.startAwait();

      stage().scheduler()
              .scheduleOnce(selfAs(Scheduled.class), null, 100L, initializer.properties().clusterStartupPeriod());
    }
  }

  @Override
  public void handleInboundStreamMessage(AddressType addressType, RawMessage message) {
    if (isStopped()) {
      return;
    }

    if (addressType.isOperational()) {
      final String textMessage = message.asTextMessage();
      final OperationalMessage typedMessage = OperationalMessage.messageFrom(textMessage);
      if (typedMessage != null) {
        if (typedMessage.isApp()) {
          attributesAgent.handleInboundStreamMessage(addressType, message);
        }
      }
    } else if (addressType.isApplication()) {
      clusterApplication.handleApplicationMessage(message);
    } else {
      logger().warn("ClusterActor couldn't dispatch incoming message; unknown address type: " +
              addressType + " for message: " + message.asTextMessage());
    }
  }

  @Override
  public void intervalSignal(Scheduled<Object> scheduled, Object data) {
    registry.startupIsCompleted();
    clusterApplication.informClusterIsHealthy(registry.isClusterHealthy());
  }

  @Override
  public void shutDown() {
    if (isStopped()) {
      return;
    }

    clusterApplication.stop();
    communicationsHub.close();
    if (cluster != null) cluster.shutdown();

    stop();
    stage().world().terminate();
  }
}

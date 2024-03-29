// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.scalecube.cluster.Cluster;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.common.pool.ElasticResourcePool;
import io.vlingo.xoom.wire.fdx.inbound.InboundStream;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.fdx.inbound.rsocket.RSocketInboundChannelReaderProvider;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.fdx.outbound.rsocket.ManagedOutboundRSocketChannelProvider;
import io.vlingo.xoom.wire.message.ConsumerByteBufferPool;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Node;

public class ClusterCommunicationsHub {
  static final String APP_NAME = "APP";

  private InboundStream applicationInboundStream;
  private ManagedOutboundRSocketChannelProvider outboundChannelProvider;
  private ApplicationOutboundStream applicationOutboundStream;
  private OperationalOutboundStream operationalOutboundStream = null; // null when single node

  private final Properties properties;

  ClusterCommunicationsHub(Properties properties) {
    this.properties = properties;
  }

  public void close() {
    applicationInboundStream.stop();
    applicationOutboundStream.stop();
    if (operationalOutboundStream != null) {
      operationalOutboundStream.stop();
    }
  }

  public void openAppChannel(
      final Stage stage,
      final Node node,
      final InboundStreamInterest interest)
      throws Exception {

    final Logger logger = stage.world().defaultLogger();

    this.applicationInboundStream = InboundStream.instance(
            stage,
            new RSocketInboundChannelReaderProvider(properties.applicationBufferSize(), logger),
            interest,
            node.applicationAddress().port(),
            AddressType.APP,
            APP_NAME,
            properties.applicationInboundProbeInterval());

    this.outboundChannelProvider = new ManagedOutboundRSocketChannelProvider(node, AddressType.APP, logger);

    this.applicationOutboundStream = ApplicationOutboundStream.instance(
            stage,
            this.outboundChannelProvider,
            new ConsumerByteBufferPool(
                ElasticResourcePool.Config.of(properties.applicationOutgoingPooledBuffers()),
                properties.applicationBufferSize()));
  }

  public void openOpChannel(
          final Stage stage,
          final Registry registry,
          final Cluster cluster) {
    this.operationalOutboundStream = OperationalOutboundStream.instance(
            stage,
            cluster,
            registry,
            new ConsumerByteBufferPool(
                    ElasticResourcePool.Config.of(properties.operationalOutgoingPooledBuffers()),
                    properties.operationalBufferSize()));
  }

  public InboundStream applicationInboundStream() {
    return applicationInboundStream;
  }

  public ManagedOutboundRSocketChannelProvider outboundChannelProvider() {
    return outboundChannelProvider;
  }

  public ApplicationOutboundStream applicationOutboundStream() {
    return applicationOutboundStream;
  }

  public OperationalOutboundStream operationalOutboundStream() {
    return operationalOutboundStream;
  }
}

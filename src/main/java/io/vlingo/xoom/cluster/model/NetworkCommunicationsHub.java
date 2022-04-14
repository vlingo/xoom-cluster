// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.common.pool.ElasticResourcePool;
import io.vlingo.xoom.wire.fdx.inbound.InboundStream;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.fdx.inbound.rsocket.RSocketInboundChannelReaderProvider;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.fdx.outbound.rsocket.ManagedOutboundRSocketChannelProvider;
import io.vlingo.xoom.wire.message.ConsumerByteBufferPool;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Configuration;
import io.vlingo.xoom.wire.node.Node;

class NetworkCommunicationsHub implements CommunicationsHub {
  static final String APP_NAME = "APP";
  static final String OP_NAME = "OP";

  private InboundStream applicationInboundStream;
  private ApplicationOutboundStream applicationOutboundStream;
  private InboundStream operationalInboundStream;
  private OperationalOutboundStream operationalOutboundStream;

  private final Properties properties;

  NetworkCommunicationsHub(final Properties properties) {
    this.properties = properties;
  }

  @Override
  public void close() {
    operationalInboundStream.stop();
    operationalOutboundStream.stop();
    applicationInboundStream.stop();
    applicationOutboundStream.stop();
  }

  @Override
  public void open(
          final Stage stage,
          final Node node,
          final InboundStreamInterest interest,
          final Configuration configuration)
  throws Exception {

    final Logger logger = stage.world().defaultLogger();

    this.operationalInboundStream =
            InboundStream.instance(
                    stage,
                    new RSocketInboundChannelReaderProvider(properties.operationalBufferSize(), logger),
                    interest,
                    node.operationalAddress().port(),
                    AddressType.OP,
                    OP_NAME,
                    properties.operationalInboundProbeInterval());

    this.operationalOutboundStream =
            OperationalOutboundStream.instance(
                    stage,
                    node,
                    new ManagedOutboundRSocketChannelProvider(node, AddressType.OP, configuration),
                    new ConsumerByteBufferPool(
                            ElasticResourcePool.Config.of(properties.operationalOutgoingPooledBuffers()),
                            properties.operationalBufferSize()));

    this.applicationInboundStream =
            InboundStream.instance(
                    stage,
                    new RSocketInboundChannelReaderProvider(properties.applicationBufferSize(), logger),
                    interest,
                    node.applicationAddress().port(),
                    AddressType.APP,
                    APP_NAME,
                    properties.applicationInboundProbeInterval());

    this.applicationOutboundStream =
            ApplicationOutboundStream.instance(
                    stage,
                    new ManagedOutboundRSocketChannelProvider(node, AddressType.APP, configuration),
                    new ConsumerByteBufferPool(
                            ElasticResourcePool.Config.of(properties.applicationOutgoingPooledBuffers()),
                            properties.applicationBufferSize()));
  }

  @Override
  public InboundStream applicationInboundStream() {
    return applicationInboundStream;
  }

  @Override
  public ApplicationOutboundStream applicationOutboundStream() {
    return applicationOutboundStream;
  }

  @Override
  public InboundStream operationalInboundStream() {
    return operationalInboundStream;
  }

  @Override
  public OperationalOutboundStream operationalOutboundStream() {
    return operationalOutboundStream;
  }

  @Override
  public void start() {
  }
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Stage;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.wire.fdx.inbound.InboundStream;
import io.vlingo.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.wire.fdx.inbound.rsocket.RSocketInboundChannelReaderProvider;
import io.vlingo.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.wire.fdx.outbound.rsocket.ManagedOutboundRSocketChannelProvider;
import io.vlingo.wire.message.ByteBufferPool;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Configuration;
import io.vlingo.wire.node.Node;

class NetworkCommunicationsHub implements CommunicationsHub {
  static final String APP_NAME = "APP";
  static final String OP_NAME = "OP";

  private InboundStream applicationInboundStream;
  private ApplicationOutboundStream applicationOutboundStream;
  private InboundStream operationalInboundStream;
  private OperationalOutboundStream operationalOutboundStream;

  NetworkCommunicationsHub() { }

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

    final RSocketInboundChannelReaderProvider channelReaderProvider = new RSocketInboundChannelReaderProvider(
            Properties.instance.operationalBufferSize(), stage.world().defaultLogger());

    this.operationalInboundStream =
            InboundStream.instance(
                    stage,
                    channelReaderProvider,
                    interest,
                    node.operationalAddress().port(),
                    AddressType.OP,
                    OP_NAME,
                    Properties.instance.operationalInboundProbeInterval());

    this.operationalOutboundStream =
            OperationalOutboundStream.instance(
                    stage,
                    node,
                    new ManagedOutboundRSocketChannelProvider(node, AddressType.OP, configuration),
                    new ByteBufferPool(
                            Properties.instance.operationalOutgoingPooledBuffers(),
                            Properties.instance.operationalBufferSize()));

    this.applicationInboundStream =
            InboundStream.instance(
                    stage,
                    channelReaderProvider,
                    interest,
                    node.applicationAddress().port(),
                    AddressType.APP,
                    APP_NAME,
                    Properties.instance.applicationInboundProbeInterval());

    this.applicationOutboundStream =
            ApplicationOutboundStream.instance(
                    stage,
                    new ManagedOutboundRSocketChannelProvider(node, AddressType.APP, configuration),
                    new ByteBufferPool(
                            Properties.instance.applicationOutgoingPooledBuffers(),
                            Properties.instance.applicationBufferSize()));
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

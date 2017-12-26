// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Stage;
import io.vlingo.cluster.model.application.ClusterApplicationOutboundStream;
import io.vlingo.cluster.model.inbound.InboundStream;
import io.vlingo.cluster.model.inbound.InboundStreamInterest;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.cluster.model.outbound.ManagedOutboundSocketChannelProvider;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.common.message.ByteBufferPool;

class NetworkCommunicationsHub implements CommunicationsHub {
  protected static final String APP_NAME = "APP";
  protected static final String OP_NAME = "OP";
  
  private InboundStream applicationInboundStream;
  private ClusterApplicationOutboundStream applicationOutboundStream;
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
    
    this.operationalInboundStream =
            InboundStream.instance(
                    stage,
                    interest,
                    node.operationalAddress().port(),
                    AddressType.OP,
                    OP_NAME,
                    Properties.instance.operationalBufferSize());
    
    this.operationalOutboundStream =
            OperationalOutboundStream.instance(
                    stage,
                    node,
                    new ManagedOutboundSocketChannelProvider(node, AddressType.OP, configuration),
                    new ByteBufferPool(
                            Properties.instance.operationalOutgoingPooledBuffers(),
                            Properties.instance.operationalBufferSize()));
    
    this.applicationInboundStream =
            InboundStream.instance(
                    stage,
                    interest,
                    node.applicationAddress().port(),
                    AddressType.APP,
                    APP_NAME,
                    Properties.instance.applicationBufferSize());
    
    this.applicationOutboundStream =
            ClusterApplicationOutboundStream.instance(
                    stage,
                    new ManagedOutboundSocketChannelProvider(node, AddressType.APP, configuration),
                    new ByteBufferPool(
                            Properties.instance.applicationOutgoingPooledBuffers(),
                            Properties.instance.applicationBufferSize()));
  }

  @Override
  public InboundStream applicationInboundStream() {
    return applicationInboundStream;
  }

  @Override
  public ClusterApplicationOutboundStream clusterApplicationOutboundStream() {
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
    operationalInboundStream.start();
    applicationInboundStream.start();
  }
}

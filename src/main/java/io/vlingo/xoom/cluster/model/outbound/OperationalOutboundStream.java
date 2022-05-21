// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import java.util.Collection;

import io.scalecube.cluster.Cluster;
import io.vlingo.xoom.actors.ActorInstantiator;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.cluster.model.message.ApplicationSays;
import io.vlingo.xoom.common.pool.ResourcePool;
import io.vlingo.xoom.wire.fdx.outbound.ManagedOutboundChannelProvider;
import io.vlingo.xoom.wire.message.ConsumerByteBuffer;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public interface OperationalOutboundStream extends Stoppable {
  static OperationalOutboundStream instance(
      final Stage stage,
      final Cluster cluster,
      final Node node,
      final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {

    final Definition definition =
            Definition.has(
                    OperationalOutboundStreamActor.class,
                    new OperationalOutboundStreamInstantiator(cluster, node, byteBufferPool),
                    "cluster-operational-outbound-stream");

    return stage.actorFor(OperationalOutboundStream.class, definition);
  }

  class OperationalOutboundStreamInstantiator implements ActorInstantiator<OperationalOutboundStreamActor> {
    private static final long serialVersionUID = 8429839979141981981L;

    private final Cluster cluster;
    private final Node node;
    private final ResourcePool<ConsumerByteBuffer, String> byteBufferPool;

    public OperationalOutboundStreamInstantiator(
            final Cluster cluster,
            final Node node,
            final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {
      this.cluster = cluster;
      this.node = node;
      this.byteBufferPool = byteBufferPool;
    }

    @Override
    public OperationalOutboundStreamActor instantiate() {
      return new OperationalOutboundStreamActor(cluster, node, byteBufferPool);
    }

    @Override
    public Class<OperationalOutboundStreamActor> type() {
      return OperationalOutboundStreamActor.class;
    }
  }

  void close(final Id id);
  void application(final ApplicationSays says); // broadcast
  void application(final ApplicationSays says, final Collection<Node> unconfirmedNodes);
}

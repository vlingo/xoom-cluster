// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import java.util.Collection;
import java.util.Set;

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
      final Node node,
      final ManagedOutboundChannelProvider provider,
      final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {

    final Definition definition =
            Definition.has(
                    OperationalOutboundStreamActor.class,
                    new OperationalOutboundStreamInstantiator(node, provider, byteBufferPool),
                    "cluster-operational-outbound-stream");

    return stage.actorFor(OperationalOutboundStream.class, definition);
  }

  class OperationalOutboundStreamInstantiator implements ActorInstantiator<OperationalOutboundStreamActor> {
    private static final long serialVersionUID = 8429839979141981981L;

    private final Node node;
    private final ManagedOutboundChannelProvider provider;
    private final ResourcePool<ConsumerByteBuffer, String> byteBufferPool;

    public OperationalOutboundStreamInstantiator(
            final Node node,
            final ManagedOutboundChannelProvider provider,
            final ResourcePool<ConsumerByteBuffer, String> byteBufferPool) {
      this.node = node;
      this.provider = provider;
      this.byteBufferPool = byteBufferPool;
    }

    @Override
    public OperationalOutboundStreamActor instantiate() {
      return new OperationalOutboundStreamActor(node, provider, byteBufferPool);
    }

    @Override
    public Class<OperationalOutboundStreamActor> type() {
      return OperationalOutboundStreamActor.class;
    }
  }

  void close(final Id id);
  void application(final ApplicationSays says, final Collection<Node> unconfirmedNodes);
  void directory(final Set<Node> allLiveNodes);
  void elect(final Collection<Node> allGreaterNodes);
  void join();
  void leader();
  void leader(Id id);
  void leave();
  void open(final Id id);
  void ping(final Id targetNodeId);
  void pulse(final Id targetNodeId);
  void pulse();
  void split(final Id targetNodeId, final Id currentLeaderId);
  void vote(final Id targetNodeId);
}

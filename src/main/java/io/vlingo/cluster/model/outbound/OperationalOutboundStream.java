// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import java.util.Collection;
import java.util.Set;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Stoppable;
import io.vlingo.cluster.model.message.ApplicationSays;
import io.vlingo.wire.fdx.outbound.ManagedOutboundChannelProvider;
import io.vlingo.wire.message.ConsumerByteBufferPool;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

public interface OperationalOutboundStream extends Stoppable {
  public static OperationalOutboundStream instance(
          final Stage stage,
          final Node node,
          final ManagedOutboundChannelProvider provider,
          final ConsumerByteBufferPool byteBufferPool) {

    final Definition definition =
            Definition.has(
                    OperationalOutboundStreamActor.class,
                    new OperationalOutboundStreamInstantiator(node, provider, byteBufferPool),
                    "cluster-operational-outbound-stream");

    final OperationalOutboundStream operationalOutboundStream =
            stage.actorFor(OperationalOutboundStream.class, definition);

    return operationalOutboundStream;
  }

  static class OperationalOutboundStreamInstantiator implements ActorInstantiator<OperationalOutboundStreamActor> {
    private final Node node;
    private final ManagedOutboundChannelProvider provider;
    private final ConsumerByteBufferPool byteBufferPool;

    public OperationalOutboundStreamInstantiator(
            final Node node,
            final ManagedOutboundChannelProvider provider,
            final ConsumerByteBufferPool byteBufferPool) {
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

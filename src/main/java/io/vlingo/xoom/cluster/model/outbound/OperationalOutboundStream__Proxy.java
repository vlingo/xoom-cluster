// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.outbound;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.actors.Definition.SerializationProxy;
import io.vlingo.xoom.common.SerializableConsumer;

public class OperationalOutboundStream__Proxy extends ActorProxyBase<io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream> implements io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream {

  private static final String closeRepresentation3 = "close(io.vlingo.xoom.wire.node.Id)";
  private static final String applicationRepresentation14 = "application(io.vlingo.xoom.cluster.model.message.ApplicationSays, java.util.Collection<io.vlingo.xoom.wire.node.Node>)";
  private static final String stopRepresentation15 = "stop()";
  private static final String isStoppedRepresentation16 = "isStopped()";
  private static final String concludeRepresentation17 = "conclude()";

  private final Actor actor;
  private final Mailbox mailbox;

  public OperationalOutboundStream__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public OperationalOutboundStream__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  @Override
  public void close(io.vlingo.xoom.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.close(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, closeRepresentation3); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, closeRepresentation3)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, closeRepresentation3));
    }
  }
  @Override
  public void application(io.vlingo.xoom.cluster.model.message.ApplicationSays arg0, java.util.Collection<io.vlingo.xoom.wire.node.Node> arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.application(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, applicationRepresentation14); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, applicationRepresentation14)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, applicationRepresentation14));
    }
  }
  @Override
  public void stop() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = Stoppable::stop;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, stopRepresentation15); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, stopRepresentation15)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, stopRepresentation15));
    }
  }
  @Override
  public boolean isStopped() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = Stoppable::isStopped;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, isStoppedRepresentation16); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, isStoppedRepresentation16)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, isStoppedRepresentation16));
    }
    return false;
  }
  @Override
  public void conclude() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = Stoppable::conclude;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, concludeRepresentation17); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, concludeRepresentation17)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, concludeRepresentation17));
    }
  }
}

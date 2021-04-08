// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
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

  private static final String splitRepresentation1 = "split(io.vlingo.xoom.wire.node.Id, io.vlingo.xoom.wire.node.Id)";
  private static final String joinRepresentation2 = "join()";
  private static final String closeRepresentation3 = "close(io.vlingo.xoom.wire.node.Id)";
  private static final String directoryRepresentation4 = "directory(java.util.Set<io.vlingo.xoom.wire.node.Node>)";
  private static final String openRepresentation5 = "open(io.vlingo.xoom.wire.node.Id)";
  private static final String electRepresentation6 = "elect(java.util.Collection<io.vlingo.xoom.wire.node.Node>)";
  private static final String leaveRepresentation7 = "leave()";
  private static final String pingRepresentation8 = "ping(io.vlingo.xoom.wire.node.Id)";
  private static final String pulseRepresentation9 = "pulse()";
  private static final String pulseRepresentation10 = "pulse(io.vlingo.xoom.wire.node.Id)";
  private static final String voteRepresentation11 = "vote(io.vlingo.xoom.wire.node.Id)";
  private static final String leaderRepresentation12 = "leader(io.vlingo.xoom.wire.node.Id)";
  private static final String leaderRepresentation13 = "leader()";
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
  public void split(io.vlingo.xoom.wire.node.Id arg0, io.vlingo.xoom.wire.node.Id arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.split(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, splitRepresentation1); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, splitRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, splitRepresentation1));
    }
  }
  @Override
  public void join() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = OperationalOutboundStream::join;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, joinRepresentation2); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, joinRepresentation2)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, joinRepresentation2));
    }
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
  public void directory(java.util.Set<io.vlingo.xoom.wire.node.Node> arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.directory(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, directoryRepresentation4); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, directoryRepresentation4)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, directoryRepresentation4));
    }
  }
  @Override
  public void open(io.vlingo.xoom.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.open(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, openRepresentation5); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, openRepresentation5)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, openRepresentation5));
    }
  }
  @Override
  public void elect(java.util.Collection<io.vlingo.xoom.wire.node.Node> arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.elect(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, electRepresentation6); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, electRepresentation6)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, electRepresentation6));
    }
  }
  @Override
  public void leave() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = OperationalOutboundStream::leave;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, leaveRepresentation7); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, leaveRepresentation7)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, leaveRepresentation7));
    }
  }
  @Override
  public void ping(io.vlingo.xoom.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.ping(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, pingRepresentation8); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, pingRepresentation8)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, pingRepresentation8));
    }
  }
  @Override
  public void pulse() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = OperationalOutboundStream::pulse;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, pulseRepresentation9); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, pulseRepresentation9)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, pulseRepresentation9));
    }
  }
  @Override
  public void pulse(io.vlingo.xoom.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.pulse(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, pulseRepresentation10); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, pulseRepresentation10)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, pulseRepresentation10));
    }
  }
  @Override
  public void vote(io.vlingo.xoom.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.vote(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, voteRepresentation11); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, voteRepresentation11)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, voteRepresentation11));
    }
  }
  @Override
  public void leader(io.vlingo.xoom.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = (actor) -> actor.leader(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, leaderRepresentation12); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, leaderRepresentation12)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, leaderRepresentation12));
    }
  }
  @Override
  public void leader() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<OperationalOutboundStream> self = this;
      final SerializableConsumer<OperationalOutboundStream> consumer = OperationalOutboundStream::leader;
      if (mailbox.isPreallocated()) { mailbox.send(actor, OperationalOutboundStream.class, consumer, null, leaderRepresentation13); }
      else { mailbox.send(new LocalMessage<>(actor, OperationalOutboundStream.class, consumer, leaderRepresentation13)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, leaderRepresentation13));
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

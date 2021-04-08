// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.common.SerializableConsumer;

public class AttributesAgent__Proxy implements io.vlingo.xoom.cluster.model.attribute.AttributesAgent {

  private static final String representationConclude0 = "conclude()";
  private static final String addRepresentation1 = "add(java.lang.String, java.lang.String, T)";
  private static final String removeRepresentation2 = "remove(java.lang.String, java.lang.String)";
  private static final String replaceRepresentation3 = "replace(java.lang.String, java.lang.String, T)";
  private static final String removeAllRepresentation4 = "removeAll(java.lang.String)";
  private static final String synchronizeRepresentation5 = "synchronize(io.vlingo.xoom.wire.node.Node)";
  private static final String handleInboundStreamMessageRepresentation6 = "handleInboundStreamMessage(io.vlingo.xoom.wire.node.AddressType, io.vlingo.xoom.wire.message.RawMessage)";
  private static final String intervalSignalRepresentation7 = "intervalSignal(io.vlingo.xoom.common.Scheduled<T>, T)";
  private static final String stopRepresentation8 = "stop()";
  private static final String isStoppedRepresentation9 = "isStopped()";

  private final Actor actor;
  private final Mailbox mailbox;

  public AttributesAgent__Proxy(final Actor actor, final Mailbox mailbox){
    this.actor = actor;
    this.mailbox = mailbox;
  }

  @Override
  public <T>void add(java.lang.String arg0, java.lang.String arg1, T arg2) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.add(arg0, arg1, arg2);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, addRepresentation1); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, addRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, addRepresentation1));
    }
  }
  @Override
  public void remove(java.lang.String arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.remove(arg0, arg1);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, removeRepresentation2); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, removeRepresentation2)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, removeRepresentation2));
    }
  }
  @Override
  public <T>void replace(java.lang.String arg0, java.lang.String arg1, T arg2) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.replace(arg0, arg1, arg2);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, replaceRepresentation3); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, replaceRepresentation3)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, replaceRepresentation3));
    }
  }
  @Override
  public void removeAll(java.lang.String arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.removeAll(arg0);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, removeAllRepresentation4); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, removeAllRepresentation4)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, removeAllRepresentation4));
    }
  }
  @Override
  public void synchronize(io.vlingo.xoom.wire.node.Node arg0) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.synchronize(arg0);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, synchronizeRepresentation5); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, synchronizeRepresentation5)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, synchronizeRepresentation5));
    }
  }
  @Override
  public void handleInboundStreamMessage(io.vlingo.xoom.wire.node.AddressType arg0, io.vlingo.xoom.wire.message.RawMessage arg1) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.handleInboundStreamMessage(arg0, arg1);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, handleInboundStreamMessageRepresentation6); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, handleInboundStreamMessageRepresentation6)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, handleInboundStreamMessageRepresentation6));
    }
  }
  @Override
  public void intervalSignal(io.vlingo.xoom.common.Scheduled<Object> arg0, Object arg1) {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = (actor) -> actor.intervalSignal(arg0, arg1);
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, intervalSignalRepresentation7); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, intervalSignalRepresentation7)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, intervalSignalRepresentation7));
    }
  }
  @Override
  public void conclude() {
    if (!actor.isStopped()) {
      final SerializableConsumer<Stoppable> consumer = Stoppable::conclude;
      if (mailbox.isPreallocated()) { mailbox.send(actor, Stoppable.class, consumer, null, representationConclude0); }
      else { mailbox.send(new LocalMessage<>(actor, Stoppable.class, consumer, representationConclude0)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, representationConclude0));
    }
  }
  @Override
  public void stop() {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = Stoppable::stop;
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, stopRepresentation8); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, stopRepresentation8)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, stopRepresentation8));
    }
  }
  @Override
  public boolean isStopped() {
    if (!actor.isStopped()) {
      final SerializableConsumer<AttributesAgent> consumer = Stoppable::isStopped;
      if (mailbox.isPreallocated()) { mailbox.send(actor, AttributesAgent.class, consumer, null, isStoppedRepresentation9); }
      else { mailbox.send(new LocalMessage<>(actor, AttributesAgent.class, consumer, isStoppedRepresentation9)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, isStoppedRepresentation9));
    }
    return false;
  }
}

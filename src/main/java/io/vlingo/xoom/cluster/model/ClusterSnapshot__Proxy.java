// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorProxyBase;
import io.vlingo.xoom.actors.DeadLetter;
import io.vlingo.xoom.actors.Definition.SerializationProxy;
import io.vlingo.xoom.actors.LocalMessage;
import io.vlingo.xoom.actors.Mailbox;
import io.vlingo.xoom.common.SerializableConsumer;

public class ClusterSnapshot__Proxy extends ActorProxyBase<io.vlingo.xoom.cluster.model.ClusterSnapshot> implements io.vlingo.xoom.cluster.model.ClusterSnapshot {

  private static final String quorumAchievedRepresentation1 = "quorumAchieved()";
  private static final String quorumLostRepresentation2 = "quorumLost()";

  private final Actor actor;
  private final Mailbox mailbox;

  public ClusterSnapshot__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.xoom.cluster.model.ClusterSnapshot.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public ClusterSnapshot__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  @Override
  public void quorumAchieved() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterSnapshot> self = this;
      final SerializableConsumer<ClusterSnapshot> consumer = ClusterSnapshot::quorumAchieved;
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterSnapshot.class, consumer, null, quorumAchievedRepresentation1); }
      else { mailbox.send(new LocalMessage<>(actor, ClusterSnapshot.class, consumer, quorumAchievedRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, quorumAchievedRepresentation1));
    }
  }
  @Override
  public void quorumLost() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterSnapshot> self = this;
      final SerializableConsumer<ClusterSnapshot> consumer = ClusterSnapshot::quorumLost;
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterSnapshot.class, consumer, null, quorumLostRepresentation2); }
      else { mailbox.send(new LocalMessage<>(actor, ClusterSnapshot.class, consumer, quorumLostRepresentation2)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, quorumLostRepresentation2));
    }
  }
}

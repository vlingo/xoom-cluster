// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
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

public class ClusterSnapshotControl__Proxy extends ActorProxyBase<io.vlingo.xoom.cluster.model.ClusterSnapshotControl> implements io.vlingo.xoom.cluster.model.ClusterSnapshotControl {

  private static final String shutDownRepresentation1 = "shutDown()";

  private final Actor actor;
  private final Mailbox mailbox;

  public ClusterSnapshotControl__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.xoom.cluster.model.ClusterSnapshotControl.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public ClusterSnapshotControl__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  @Override
  public void shutDown() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterSnapshotControl> self = this;
      final SerializableConsumer<ClusterSnapshotControl> consumer = ClusterSnapshotControl::shutDown;
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterSnapshotControl.class, consumer, null, shutDownRepresentation1); }
      else { mailbox.send(new LocalMessage<>(actor, ClusterSnapshotControl.class, consumer, shutDownRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, shutDownRepresentation1));
    }
  }
}

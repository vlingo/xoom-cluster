// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.Definition.SerializationProxy;
import io.vlingo.actors.LocalMessage;
import io.vlingo.actors.Mailbox;
import io.vlingo.common.SerializableConsumer;

public class RegistryInterest__Proxy extends ActorProxyBase<io.vlingo.cluster.model.node.RegistryInterest> implements io.vlingo.cluster.model.node.RegistryInterest {

  private static final String informMergedAllDirectoryEntriesRepresentation1 = "informMergedAllDirectoryEntries(java.util.Collection<io.vlingo.wire.node.Node>, java.util.Collection<io.vlingo.cluster.model.node.MergeResult>, boolean)";
  private static final String informCurrentLeaderRepresentation2 = "informCurrentLeader(io.vlingo.wire.node.Node, boolean)";
  private static final String informLeaderDemotedRepresentation3 = "informLeaderDemoted(io.vlingo.wire.node.Node, boolean)";
  private static final String informConfirmedByLeaderRepresentation4 = "informConfirmedByLeader(io.vlingo.wire.node.Node, boolean)";
  private static final String informNodeTimedOutRepresentation5 = "informNodeTimedOut(io.vlingo.wire.node.Node, boolean)";
  private static final String informAllLiveNodesRepresentation6 = "informAllLiveNodes(java.util.Collection<io.vlingo.wire.node.Node>, boolean)";
  private static final String informNodeIsHealthyRepresentation7 = "informNodeIsHealthy(io.vlingo.wire.node.Node, boolean)";
  private static final String informNodeJoinedClusterRepresentation8 = "informNodeJoinedCluster(io.vlingo.wire.node.Node, boolean)";
  private static final String informNodeLeftClusterRepresentation9 = "informNodeLeftCluster(io.vlingo.wire.node.Node, boolean)";

  private final Actor actor;
  private final Mailbox mailbox;

  public RegistryInterest__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.cluster.model.node.RegistryInterest.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public RegistryInterest__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  @Override
  public void informMergedAllDirectoryEntries(java.util.Collection<io.vlingo.wire.node.Node> arg0, java.util.Collection<io.vlingo.cluster.model.node.MergeResult> arg1, boolean arg2) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informMergedAllDirectoryEntries(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1), ActorProxyBase.thunk(self, (Actor)actor, arg2));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informMergedAllDirectoryEntriesRepresentation1); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informMergedAllDirectoryEntriesRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informMergedAllDirectoryEntriesRepresentation1));
    }
  }
  @Override
  public void informCurrentLeader(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informCurrentLeader(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informCurrentLeaderRepresentation2); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informCurrentLeaderRepresentation2)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informCurrentLeaderRepresentation2));
    }
  }
  @Override
  public void informLeaderDemoted(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informLeaderDemoted(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informLeaderDemotedRepresentation3); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informLeaderDemotedRepresentation3)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informLeaderDemotedRepresentation3));
    }
  }
  @Override
  public void informConfirmedByLeader(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informConfirmedByLeader(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informConfirmedByLeaderRepresentation4); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informConfirmedByLeaderRepresentation4)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informConfirmedByLeaderRepresentation4));
    }
  }
  @Override
  public void informNodeTimedOut(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informNodeTimedOut(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informNodeTimedOutRepresentation5); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informNodeTimedOutRepresentation5)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeTimedOutRepresentation5));
    }
  }
  @Override
  public void informAllLiveNodes(java.util.Collection<io.vlingo.wire.node.Node> arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informAllLiveNodes(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informAllLiveNodesRepresentation6); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informAllLiveNodesRepresentation6)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAllLiveNodesRepresentation6));
    }
  }
  @Override
  public void informNodeIsHealthy(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informNodeIsHealthy(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informNodeIsHealthyRepresentation7); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informNodeIsHealthyRepresentation7)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeIsHealthyRepresentation7));
    }
  }
  @Override
  public void informNodeJoinedCluster(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informNodeJoinedCluster(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informNodeJoinedClusterRepresentation8); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informNodeJoinedClusterRepresentation8)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeJoinedClusterRepresentation8));
    }
  }
  @Override
  public void informNodeLeftCluster(io.vlingo.wire.node.Node arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<RegistryInterest> self = this;
      final SerializableConsumer<RegistryInterest> consumer = (actor) -> actor.informNodeLeftCluster(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, RegistryInterest.class, consumer, null, informNodeLeftClusterRepresentation9); }
      else { mailbox.send(new LocalMessage<RegistryInterest>(actor, RegistryInterest.class, consumer, informNodeLeftClusterRepresentation9)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeLeftClusterRepresentation9));
    }
  }
}

// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application;

import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.Definition.SerializationProxy;
import io.vlingo.actors.LocalMessage;
import io.vlingo.actors.Mailbox;
import io.vlingo.common.SerializableConsumer;

public class ClusterApplication__Proxy extends ActorProxyBase<io.vlingo.cluster.model.application.ClusterApplication> implements io.vlingo.cluster.model.application.ClusterApplication {

  private static final String handleApplicationMessageRepresentation1 = "handleApplicationMessage(io.vlingo.wire.message.RawMessage)";
  private static final String informResponderRepresentation2 = "informResponder(io.vlingo.wire.fdx.outbound.ApplicationOutboundStream)";
  private static final String informAllLiveNodesRepresentation3 = "informAllLiveNodes(java.util.Collection<io.vlingo.wire.node.Node>, boolean)";
  private static final String informLeaderElectedRepresentation4 = "informLeaderElected(io.vlingo.wire.node.Id, boolean, boolean)";
  private static final String informLeaderLostRepresentation5 = "informLeaderLost(io.vlingo.wire.node.Id, boolean)";
  private static final String informLocalNodeShutDownRepresentation6 = "informLocalNodeShutDown(io.vlingo.wire.node.Id)";
  private static final String informLocalNodeStartedRepresentation7 = "informLocalNodeStarted(io.vlingo.wire.node.Id)";
  private static final String informNodeIsHealthyRepresentation8 = "informNodeIsHealthy(io.vlingo.wire.node.Id, boolean)";
  private static final String informNodeJoinedClusterRepresentation9 = "informNodeJoinedCluster(io.vlingo.wire.node.Id, boolean)";
  private static final String informNodeLeftClusterRepresentation10 = "informNodeLeftCluster(io.vlingo.wire.node.Id, boolean)";
  private static final String informQuorumAchievedRepresentation11 = "informQuorumAchieved()";
  private static final String informQuorumLostRepresentation12 = "informQuorumLost()";
  private static final String informAttributesClientRepresentation13 = "informAttributesClient(io.vlingo.cluster.model.attribute.AttributesProtocol)";
  private static final String informAttributeSetCreatedRepresentation14 = "informAttributeSetCreated(java.lang.String)";
  private static final String informAttributeAddedRepresentation15 = "informAttributeAdded(java.lang.String, java.lang.String)";
  private static final String informAttributeRemovedRepresentation16 = "informAttributeRemoved(java.lang.String, java.lang.String)";
  private static final String informAttributeSetRemovedRepresentation17 = "informAttributeSetRemoved(java.lang.String)";
  private static final String informAttributeReplacedRepresentation18 = "informAttributeReplaced(java.lang.String, java.lang.String)";
  private static final String startRepresentation19 = "start()";
  private static final String stopRepresentation20 = "stop()";
  private static final String concludeRepresentation21 = "conclude()";
  private static final String isStoppedRepresentation22 = "isStopped()";

  private final Actor actor;
  private final Mailbox mailbox;

  public ClusterApplication__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.cluster.model.application.ClusterApplication.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public ClusterApplication__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  @Override
  public void handleApplicationMessage(io.vlingo.wire.message.RawMessage arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.handleApplicationMessage(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, handleApplicationMessageRepresentation1); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, handleApplicationMessageRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, handleApplicationMessageRepresentation1));
    }
  }
  @Override
  public void informResponder(io.vlingo.wire.fdx.outbound.ApplicationOutboundStream arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informResponder(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informResponderRepresentation2); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informResponderRepresentation2)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informResponderRepresentation2));
    }
  }
  @Override
  public void informAllLiveNodes(java.util.Collection<io.vlingo.wire.node.Node> arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAllLiveNodes(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAllLiveNodesRepresentation3); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAllLiveNodesRepresentation3)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAllLiveNodesRepresentation3));
    }
  }
  @Override
  public void informLeaderElected(io.vlingo.wire.node.Id arg0, boolean arg1, boolean arg2) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informLeaderElected(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1), ActorProxyBase.thunk(self, (Actor)actor, arg2));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informLeaderElectedRepresentation4); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informLeaderElectedRepresentation4)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informLeaderElectedRepresentation4));
    }
  }
  @Override
  public void informLeaderLost(io.vlingo.wire.node.Id arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informLeaderLost(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informLeaderLostRepresentation5); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informLeaderLostRepresentation5)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informLeaderLostRepresentation5));
    }
  }
  @Override
  public void informLocalNodeShutDown(io.vlingo.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informLocalNodeShutDown(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informLocalNodeShutDownRepresentation6); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informLocalNodeShutDownRepresentation6)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informLocalNodeShutDownRepresentation6));
    }
  }
  @Override
  public void informLocalNodeStarted(io.vlingo.wire.node.Id arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informLocalNodeStarted(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informLocalNodeStartedRepresentation7); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informLocalNodeStartedRepresentation7)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informLocalNodeStartedRepresentation7));
    }
  }
  @Override
  public void informNodeIsHealthy(io.vlingo.wire.node.Id arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informNodeIsHealthy(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informNodeIsHealthyRepresentation8); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informNodeIsHealthyRepresentation8)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeIsHealthyRepresentation8));
    }
  }
  @Override
  public void informNodeJoinedCluster(io.vlingo.wire.node.Id arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informNodeJoinedCluster(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informNodeJoinedClusterRepresentation9); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informNodeJoinedClusterRepresentation9)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeJoinedClusterRepresentation9));
    }
  }
  @Override
  public void informNodeLeftCluster(io.vlingo.wire.node.Id arg0, boolean arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informNodeLeftCluster(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informNodeLeftClusterRepresentation10); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informNodeLeftClusterRepresentation10)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informNodeLeftClusterRepresentation10));
    }
  }
  @Override
  public void informQuorumAchieved() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informQuorumAchieved();
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informQuorumAchievedRepresentation11); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informQuorumAchievedRepresentation11)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informQuorumAchievedRepresentation11));
    }
  }
  @Override
  public void informQuorumLost() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informQuorumLost();
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informQuorumLostRepresentation12); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informQuorumLostRepresentation12)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informQuorumLostRepresentation12));
    }
  }
  @Override
  public void informAttributesClient(io.vlingo.cluster.model.attribute.AttributesProtocol arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAttributesClient(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAttributesClientRepresentation13); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAttributesClientRepresentation13)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAttributesClientRepresentation13));
    }
  }
  @Override
  public void informAttributeSetCreated(java.lang.String arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAttributeSetCreated(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAttributeSetCreatedRepresentation14); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAttributeSetCreatedRepresentation14)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAttributeSetCreatedRepresentation14));
    }
  }
  @Override
  public void informAttributeAdded(java.lang.String arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAttributeAdded(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAttributeAddedRepresentation15); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAttributeAddedRepresentation15)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAttributeAddedRepresentation15));
    }
  }
  @Override
  public void informAttributeRemoved(java.lang.String arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAttributeRemoved(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAttributeRemovedRepresentation16); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAttributeRemovedRepresentation16)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAttributeRemovedRepresentation16));
    }
  }
  @Override
  public void informAttributeSetRemoved(java.lang.String arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAttributeSetRemoved(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAttributeSetRemovedRepresentation17); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAttributeSetRemovedRepresentation17)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAttributeSetRemovedRepresentation17));
    }
  }
  @Override
  public void informAttributeReplaced(java.lang.String arg0, java.lang.String arg1) {
    if (!actor.isStopped()) {
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.informAttributeReplaced(ActorProxyBase.thunk(self, (Actor)actor, arg0), ActorProxyBase.thunk(self, (Actor)actor, arg1));
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, informAttributeReplacedRepresentation18); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, informAttributeReplacedRepresentation18)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, informAttributeReplacedRepresentation18));
    }
  }
  @Override
  public void start() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.start();
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, startRepresentation19); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, startRepresentation19)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, startRepresentation19));
    }
  }
  @Override
  public void stop() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.stop();
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, stopRepresentation20); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, stopRepresentation20)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, stopRepresentation20));
    }
  }
  @Override
  public void conclude() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.conclude();
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, concludeRepresentation21); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, concludeRepresentation21)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, concludeRepresentation21));
    }
  }
  @Override
  public boolean isStopped() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<ClusterApplication> self = this;
      final SerializableConsumer<ClusterApplication> consumer = (actor) -> actor.isStopped();
      if (mailbox.isPreallocated()) { mailbox.send(actor, ClusterApplication.class, consumer, null, isStoppedRepresentation22); }
      else { mailbox.send(new LocalMessage<ClusterApplication>(actor, ClusterApplication.class, consumer, isStoppedRepresentation22)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, isStoppedRepresentation22));
    }
    return false;
  }
}

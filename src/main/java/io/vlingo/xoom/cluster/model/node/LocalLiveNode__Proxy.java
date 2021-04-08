package io.vlingo.xoom.cluster.model.node;

import io.vlingo.xoom.actors.*;
import io.vlingo.xoom.actors.Definition.SerializationProxy;
import io.vlingo.xoom.common.SerializableConsumer;

public class LocalLiveNode__Proxy extends ActorProxyBase<io.vlingo.xoom.cluster.model.node.LocalLiveNode> implements io.vlingo.xoom.cluster.model.node.LocalLiveNode {

  private static final String handleRepresentation1 = "handle(io.vlingo.xoom.cluster.model.message.OperationalMessage)";
  private static final String registerNodeSynchronizerRepresentation2 = "registerNodeSynchronizer(io.vlingo.xoom.wire.node.NodeSynchronizer)";
  private static final String stopRepresentation3 = "stop()";
  private static final String isStoppedRepresentation4 = "isStopped()";
  private static final String concludeRepresentation5 = "conclude()";

  private final Actor actor;
  private final Mailbox mailbox;

  public LocalLiveNode__Proxy(final Actor actor, final Mailbox mailbox){
    super(io.vlingo.xoom.cluster.model.node.LocalLiveNode.class, SerializationProxy.from(actor.definition()), actor.address());
    this.actor = actor;
    this.mailbox = mailbox;
  }

  public LocalLiveNode__Proxy(){
    super();
    this.actor = null;
    this.mailbox = null;
  }

  @Override
  public void handle(io.vlingo.xoom.cluster.model.message.OperationalMessage arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<LocalLiveNode> self = this;
      final SerializableConsumer<LocalLiveNode> consumer = (actor) -> actor.handle(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, LocalLiveNode.class, consumer, null, handleRepresentation1); }
      else { mailbox.send(new LocalMessage<>(actor, LocalLiveNode.class, consumer, handleRepresentation1)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, handleRepresentation1));
    }
  }
  @Override
  public void registerNodeSynchronizer(io.vlingo.xoom.wire.node.NodeSynchronizer arg0) {
    if (!actor.isStopped()) {
      ActorProxyBase<LocalLiveNode> self = this;
      final SerializableConsumer<LocalLiveNode> consumer = (actor) -> actor.registerNodeSynchronizer(ActorProxyBase.thunk(self, (Actor)actor, arg0));
      if (mailbox.isPreallocated()) { mailbox.send(actor, LocalLiveNode.class, consumer, null, registerNodeSynchronizerRepresentation2); }
      else { mailbox.send(new LocalMessage<>(actor, LocalLiveNode.class, consumer, registerNodeSynchronizerRepresentation2)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, registerNodeSynchronizerRepresentation2));
    }
  }
  @Override
  public void stop() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<LocalLiveNode> self = this;
      final SerializableConsumer<LocalLiveNode> consumer = Stoppable::stop;
      if (mailbox.isPreallocated()) { mailbox.send(actor, LocalLiveNode.class, consumer, null, stopRepresentation3); }
      else { mailbox.send(new LocalMessage<>(actor, LocalLiveNode.class, consumer, stopRepresentation3)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, stopRepresentation3));
    }
  }
  @Override
  public boolean isStopped() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<LocalLiveNode> self = this;
      final SerializableConsumer<LocalLiveNode> consumer = Stoppable::isStopped;
      if (mailbox.isPreallocated()) { mailbox.send(actor, LocalLiveNode.class, consumer, null, isStoppedRepresentation4); }
      else { mailbox.send(new LocalMessage<>(actor, LocalLiveNode.class, consumer, isStoppedRepresentation4)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, isStoppedRepresentation4));
    }
    return false;
  }
  @Override
  public void conclude() {
    if (!actor.isStopped()) {
      @SuppressWarnings("unused")
      ActorProxyBase<LocalLiveNode> self = this;
      final SerializableConsumer<LocalLiveNode> consumer = Stoppable::conclude;
      if (mailbox.isPreallocated()) { mailbox.send(actor, LocalLiveNode.class, consumer, null, concludeRepresentation5); }
      else { mailbox.send(new LocalMessage<>(actor, LocalLiveNode.class, consumer, concludeRepresentation5)); }
    } else {
      actor.deadLetters().failedDelivery(new DeadLetter(actor, concludeRepresentation5));
    }
  }
}

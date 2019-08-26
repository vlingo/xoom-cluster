// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import io.vlingo.actors.Actor;
import io.vlingo.cluster.model.Properties;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.cluster.model.attribute.message.ApplicationMessageType;
import io.vlingo.cluster.model.attribute.message.ReceivedAttributeMessage;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.common.Scheduled;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Configuration;
import io.vlingo.wire.node.Node;

public class AttributesAgentActor extends Actor implements AttributesAgent {
  private final AttributesClient client;
  private final Configuration configuration;
  private final ConfirmationInterest confirmationInterest;
  private final ConfirmingDistributor confirmingDistributor;
  private final Node node;
  private final RemoteAttributeRequestHandler remoteRequestHandler;
  private final AttributeSetRepository repository;
  
  public AttributesAgentActor(
          final Node node,
          final ClusterApplication application,
          final OperationalOutboundStream outbound,
          final Configuration configuration) {
    this(node, application, outbound, configuration, new NoOpConfirmationInterest(configuration));
  }

  @SuppressWarnings("unchecked")
  public AttributesAgentActor(
          final Node node,
          final ClusterApplication application,
          final OperationalOutboundStream outbound,
          final Configuration configuration,
          final ConfirmationInterest confirmationInterest) {
    
    this.node = node;
    this.configuration = configuration;
    this.confirmationInterest = confirmationInterest;
    this.client = AttributesClient.with(selfAs(AttributesAgent.class));
    this.confirmingDistributor = new ConfirmingDistributor(application, node, outbound, configuration);
    this.repository = new AttributeSetRepository();
    this.remoteRequestHandler = new RemoteAttributeRequestHandler(confirmingDistributor, configuration, repository);
    
    application.informAttributesClient(this.client);
    
    stage().scheduler()
      .schedule(selfAs(Scheduled.class), null, 1000L, Properties.instance.clusterAttributesRedistributionInterval());
  }

  //=========================================
  // AttributesAgent (core)
  //=========================================

  @Override
  public <T> void add(final String attributeSetName, final String attributeName, final T value) {
    final AttributeSet set = repository.attributeSetOf(attributeSetName);
    
    if (set.isNone()) {
      final AttributeSet newSet = AttributeSet.named(attributeSetName);
      newSet.addIfAbsent(Attribute.from(attributeName, value));
      repository.add(newSet);
      client.syncWith(newSet);
      confirmingDistributor.distributeCreate(newSet);
    } else {
      final TrackedAttribute newlyTracked = set.addIfAbsent(Attribute.from(attributeName, value));
      if (!newlyTracked.isDistributed()) {
        confirmingDistributor.distribute(set, newlyTracked, ApplicationMessageType.AddAttribute);
      }
    }
  }

  @Override
  public <T> void replace(final String attributeSetName, final String attributeName, final T value) {
    final AttributeSet set = repository.attributeSetOf(attributeSetName);
    
    if (!set.isNone()) {
      final TrackedAttribute tracked = set.attributeNamed(attributeName);
      
      if (tracked.isPresent()) {
        final Attribute<T> other = Attribute.from(attributeName, value);
        
        if (!tracked.sameAs(other)) {
          final TrackedAttribute newlyTracked = set.replace(tracked.replacingValueWith(other));
          
          if (newlyTracked.isPresent()) {
            client.syncWith(set);
            confirmingDistributor.distribute(set, newlyTracked, ApplicationMessageType.ReplaceAttribute);
          }
        }
      }
    }
  }

  @Override
  public <T> void remove(final String attributeSetName, final String attributeName) {
    final AttributeSet set = repository.attributeSetOf(attributeSetName);
    
    if (!set.isNone()) {
      final TrackedAttribute tracked = set.attributeNamed(attributeName);
      
      if (tracked.isPresent()) {
        final TrackedAttribute untracked = set.remove(tracked.attribute);
        
        if (untracked.isPresent()) {
          client.syncWith(set);
          confirmingDistributor.distribute(set, untracked, ApplicationMessageType.RemoveAttribute);
        }
      }
    }    
  }

  @Override
  public <T> void removeAll(final String attributeSetName) {
    final AttributeSet set = repository.attributeSetOf(attributeSetName);
    
    if (!set.isNone()) {
      repository.remove(attributeSetName);
      client.syncWithout(set);
      confirmingDistributor.distributeRemove(set);
    }    
  }

  //=========================================
  // NodeSynchronizer
  //=========================================

  @Override
  public void synchronize(final Node nodeToSynchronize) {
    if (!node.equals(nodeToSynchronize)) {
      confirmingDistributor.synchronizeTo(repository.all(), nodeToSynchronize);
    }
  }


  //=========================================
  // InboundStreamInterest (operations App)
  //=========================================

  @Override
  public void handleInboundStreamMessage(final AddressType addressType, final RawMessage message) {
    if (addressType.isOperational()) {
      final ReceivedAttributeMessage request = new ReceivedAttributeMessage(message);
      final ApplicationMessageType type = request.type();
      
      switch (type) {
      case CreateAttributeSet:
        remoteRequestHandler.createAttributeSet(request);
        break;
      case AddAttribute:
        remoteRequestHandler.addAttribute(request);
        break;
      case ReplaceAttribute:
        remoteRequestHandler.replaceAttribute(request);
        break;
      case RemoveAttribute:
        remoteRequestHandler.removeAttribute(request);
        break;
      case RemoveAttributeSet:
        remoteRequestHandler.removeAttributeSet(request);
        break;
      case ConfirmCreateAttributeSet:
      case ConfirmAddAttribute:
      case ConfirmReplaceAttribute:
      case ConfirmRemoveAttribute:
      case ConfirmRemoveAttributeSet:
        confirmingDistributor.acknowledgeConfirmation(request.correlatingMessageId(), configuration.nodeMatching(request.sourceNodeId()));
        confirmationInterest.confirm(request.sourceNodeId(), request.attributeSetName(), request.attributeName(), type);
        break;
      default:
        configuration.logger().warn("Received unknown message: " + type.name());
        break;
      }
    }
  }

  //=========================================
  // Scheduled
  //=========================================

  @Override
  public void intervalSignal(final Scheduled<Object> scheduled, final Object data) {
    confirmingDistributor.redistributeUnconfirmed();
  }

  //=========================================
  // Stoppable
  //=========================================

  @Override
  public void stop() {
    if (isStopped()) {
      return;
    }
    
    repository.removeAll();
    
    super.stop();
  }
}

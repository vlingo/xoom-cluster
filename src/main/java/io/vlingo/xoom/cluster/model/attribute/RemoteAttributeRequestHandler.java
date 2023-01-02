// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cluster.model.attribute.message.ReceivedAttributeMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.function.Function;

final class RemoteAttributeRequestHandler {
  private final ConfirmingDistributor confirmingDistributor;
  private final AttributeSetRepository repository;
  private final Function<Id, Node> nodeLookup;
  private final Logger logger;
  
  RemoteAttributeRequestHandler(final ConfirmingDistributor confirmingDistributor, final AttributeSetRepository repository,
                                final Function<Id, Node> nodeLookup, final Logger logger) {
    this.confirmingDistributor = confirmingDistributor;
    this.repository = repository;
    this.nodeLookup = nodeLookup;
    this.logger = logger;
  }

  void addAttribute(final ReceivedAttributeMessage request) {
    final Node sourceNode = nodeLookup.apply(request.sourceNodeId());
    if (sourceNode == null) {
      logger.warn("Failed to addAttribute because source node " + request.sourceNodeId() + " is not part of the cluster anymore!");
    } else {
      AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
      if (attributeSet.isNone()) {
        attributeSet = AttributeSet.named(request.attributeSetName());
        repository.add(attributeSet);
      }
      final TrackedAttribute tracked = attributeSet.addIfAbsent(request.attribute());
      confirmingDistributor.confirm(request.trackingId(), attributeSet, tracked, request.type(), sourceNode);
    }
  }

  void createAttributeSet(final ReceivedAttributeMessage request) {
    final Node sourceNode = nodeLookup.apply(request.sourceNodeId());
    if (sourceNode == null) {
      logger.warn("Failed to createAttributeSet because source node " + request.sourceNodeId() + " is not part of the cluster anymore!");
    } else {
      AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
      if (attributeSet.isNone()) {
        attributeSet = AttributeSet.named(request.attributeSetName());
        repository.add(attributeSet);
      }
      confirmingDistributor.confirmCreate(request.trackingId(), attributeSet, sourceNode);
    }
  }

  void removeAttributeSet(final ReceivedAttributeMessage request) {
    final Node sourceNode = nodeLookup.apply(request.sourceNodeId());
    if (sourceNode == null) {
      logger.warn("Failed to removeAttributeSet because source node " + request.sourceNodeId() + " is not part of the cluster anymore!");
    } else {
      AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
      if (attributeSet.isDefined()) {
        attributeSet = AttributeSet.named(request.attributeSetName());
        repository.remove(request.attributeSetName());
      }
      confirmingDistributor.confirmRemove(request.trackingId(), attributeSet, sourceNode);
    }
  }

  void replaceAttribute(final ReceivedAttributeMessage request) {
    final AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
    if (attributeSet.isDefined()) {
      final Node sourceNode = nodeLookup.apply(request.sourceNodeId());
      if (sourceNode == null) {
        logger.warn("Failed to replaceAttribute because source node " + request.sourceNodeId() + " is not part of the cluster anymore!");
      } else {
        final TrackedAttribute tracked = attributeSet.replace(request.attribute());
        if (tracked.isPresent()) { // was both present and replaced
          confirmingDistributor.confirm(request.trackingId(), attributeSet, tracked, request.type(), sourceNode);
        }
      }
    }
  }

  void removeAttribute(final ReceivedAttributeMessage request) {
    final AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
    if (attributeSet.isDefined()) {
      final Node sourceNode = nodeLookup.apply(request.sourceNodeId());
      if (sourceNode == null) {
        logger.warn("Failed to removeAttribute because source node " + request.sourceNodeId() + " is not part of the cluster anymore!");
      } else {
        final TrackedAttribute tracked = attributeSet.remove(request.attribute());
        if (tracked.isPresent()) { // actually was present, now removed
          confirmingDistributor.confirm(request.trackingId(), attributeSet, tracked, request.type(), sourceNode);
        }
      }
    }
  }
}

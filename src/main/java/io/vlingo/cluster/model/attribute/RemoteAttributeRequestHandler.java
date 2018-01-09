// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import io.vlingo.cluster.model.attribute.message.ReceivedAttributeMessage;

final class RemoteAttributeRequestHandler {
  private final ConfirmingDistributor confirmingDistributor;
  private final AttributeSetRepository repository;
  
  RemoteAttributeRequestHandler(final ConfirmingDistributor confirmingDistributor, final AttributeSetRepository repository) {
    this.confirmingDistributor = confirmingDistributor;
    this.repository = repository;
  }

  protected void addAttribute(final ReceivedAttributeMessage request) {
    AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
    if (attributeSet.isNone()) {
      attributeSet = AttributeSet.named(request.attributeSetName());
      repository.add(attributeSet);
    }
    final TrackedAttribute tracked = attributeSet.addIfAbsent(request.attribute());
    confirmingDistributor.confirm(request.trackingId(), attributeSet, tracked, request.type(), request.sourceNode());
  }

  protected void createAttributeSet(final ReceivedAttributeMessage request) {
    AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
    if (attributeSet.isNone()) {
      attributeSet = AttributeSet.named(request.attributeSetName());
      repository.add(attributeSet);
    }
    confirmingDistributor.confirm(request.trackingId(), attributeSet, request.sourceNode());
  }

  protected void replaceAttribute(final ReceivedAttributeMessage request) {
    final AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
    if (attributeSet.isDefined()) {
      final TrackedAttribute tracked = attributeSet.replace(request.attribute());
      if (tracked.isPresent()) { // was both present and replaced
        confirmingDistributor.confirm(request.trackingId(), attributeSet, tracked, request.type(), request.sourceNode());
      }
    }
  }

  protected void removeAttribute(final ReceivedAttributeMessage request) {
    final AttributeSet attributeSet = repository.attributeSetOf(request.attributeSetName());
    if (attributeSet.isDefined()) {
      final TrackedAttribute tracked = attributeSet.remove(request.attribute());
      if (tracked.isPresent()) { // actually was present, now removed
        confirmingDistributor.confirm(request.trackingId(), attributeSet, tracked, request.type(), request.sourceNode());
      }
    }
  }
}

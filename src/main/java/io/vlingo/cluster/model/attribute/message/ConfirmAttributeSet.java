// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute.message;

import io.vlingo.cluster.model.attribute.AttributeSet;
import io.vlingo.cluster.model.node.Node;

public final class ConfirmAttributeSet extends ApplicationMessage {
  public final String attributeSetName;
  
  public ConfirmAttributeSet(final String correlatingMessageId, final Node node, final AttributeSet set) {
    super(correlatingMessageId, ApplicationMessageType.ConfirmCreateAttributeSet, trackingId(node, ApplicationMessageType.ConfirmCreateAttributeSet, set.name));
    this.attributeSetName = set.name;
  }
  
  @Override
  public String toPayload() {
    final StringBuffer buffer = new StringBuffer();
    
    buffer
      .append(getClass().getSimpleName())
      .append("\n")
      .append(correlatingMessageId)
      .append("\n")
      .append(trackingId)
      .append("\n")
      .append(type.name())
      .append("\n")
      .append(attributeSetName);
    
    return buffer.toString();
  }
}

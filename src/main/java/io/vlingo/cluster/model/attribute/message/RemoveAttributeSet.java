// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute.message;

import io.vlingo.cluster.model.attribute.AttributeSet;
import io.vlingo.wire.node.Node;

public class RemoveAttributeSet extends ApplicationMessage {
  public final String attributeSetName;
  
  public static RemoveAttributeSet from(final Node node, final AttributeSet set) {
    return new RemoveAttributeSet(node, set);
  }
  
  public RemoveAttributeSet(final Node node, final AttributeSet set) {
    super(NoCorrelatingMessageId, ApplicationMessageType.RemoveAttributeSet, trackingId(node, ApplicationMessageType.CreateAttributeSet, set.name));
    
    this.attributeSetName = set.name;
  }
  
  @Override
  public String toPayload() {
    final StringBuffer buffer = new StringBuffer();
    
    buffer
      .append(getClass().getSimpleName())
      .append("\n")
      .append(trackingId)
      .append("\n")
      .append(type.name())
      .append("\n")
      .append(attributeSetName);
    
    return buffer.toString();
  }
}

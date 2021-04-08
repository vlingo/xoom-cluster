// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute.message;

import io.vlingo.xoom.cluster.model.attribute.AttributeSet;
import io.vlingo.xoom.cluster.model.attribute.TrackedAttribute;
import io.vlingo.xoom.wire.node.Node;

public abstract class AttributeMessage extends ApplicationMessage {
  public final String attributeSetName;
  public final String attributeName;
  public final String attributeType;
  public final String attributeValue;
  
  @Override
  public String toPayload() {
    return getClass().getSimpleName() +
        "\n" +
        correlatingMessageId +
        "\n" +
        trackingId +
        "\n" +
        type.name() +
        "\n" +
        attributeSetName +
        "\n" +
        attributeName +
        "\n" +
        attributeType +
        "\n" +
        attributeValue;
  }

  protected AttributeMessage(final Node node, final AttributeSet set, final TrackedAttribute tracked, final ApplicationMessageType type) {
    this(NoCorrelatingMessageId, node, set, tracked, type);
  }

  protected AttributeMessage(final String correlatingMessageId, final Node node, final AttributeSet set, final TrackedAttribute tracked, final ApplicationMessageType type) {
    super(correlatingMessageId, type, trackingId(node, type, tracked.id));
    
    this.attributeSetName = set.name;
    this.attributeName = tracked.attribute.name;
    this.attributeType = tracked.attribute.type.toString();
    this.attributeValue = "" + tracked.attribute.value;
  }
}

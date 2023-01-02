// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute.message;

import io.vlingo.xoom.cluster.model.attribute.AttributeSet;
import io.vlingo.xoom.wire.node.Node;

public final class ConfirmCreateAttributeSet extends ApplicationMessage {
  public final String attributeSetName;
  
  public ConfirmCreateAttributeSet(final String correlatingMessageId, final Node node, final AttributeSet set) {
    super(correlatingMessageId, ApplicationMessageType.ConfirmCreateAttributeSet, trackingId(node, ApplicationMessageType.ConfirmCreateAttributeSet, set.name));
    this.attributeSetName = set.name;
  }
  
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
        attributeSetName;
  }
}

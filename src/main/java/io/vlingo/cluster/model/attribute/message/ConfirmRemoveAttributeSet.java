// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute.message;

import io.vlingo.cluster.model.attribute.AttributeSet;
import io.vlingo.wire.node.Node;

public final class ConfirmRemoveAttributeSet extends ApplicationMessage {
  public final String attributeSetName;
  
  public ConfirmRemoveAttributeSet(final String correlatingMessageId, final Node node, final AttributeSet set) {
    super(correlatingMessageId, ApplicationMessageType.ConfirmRemoveAttributeSet, trackingId(node, ApplicationMessageType.ConfirmRemoveAttributeSet, set.name));
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

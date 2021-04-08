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

public class ConfirmAttribute extends AttributeMessage {
  public static ConfirmAttribute from(final String correlatingMessageId, final Node node, final AttributeSet set, final TrackedAttribute tracked, final ApplicationMessageType type) {
    return new ConfirmAttribute(correlatingMessageId, node, set, tracked, type);
  }
  
  public ConfirmAttribute(final String correlatingMessageId, final Node node, final AttributeSet set, final TrackedAttribute tracked, final ApplicationMessageType type) {
    super(correlatingMessageId, node, set, tracked, type);
  }
}

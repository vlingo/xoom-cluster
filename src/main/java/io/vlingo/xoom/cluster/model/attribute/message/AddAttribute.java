// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute.message;

import io.vlingo.xoom.cluster.model.attribute.AttributeSet;
import io.vlingo.xoom.cluster.model.attribute.TrackedAttribute;
import io.vlingo.xoom.wire.node.Node;

public final class AddAttribute extends AttributeMessage {
  public static AddAttribute from(final Node node, final AttributeSet set, final TrackedAttribute tracked) {
    return new AddAttribute(node, set, tracked);
  }
  
  public AddAttribute(final Node node, final AttributeSet set, final TrackedAttribute tracked) {
    super(node, set, tracked, ApplicationMessageType.AddAttribute);
  }
}

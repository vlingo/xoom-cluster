// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute.message;

import io.vlingo.xoom.cluster.model.attribute.AttributeSet;
import io.vlingo.xoom.cluster.model.attribute.TrackedAttribute;
import io.vlingo.xoom.wire.node.Node;

public class RemoveAttribute extends AttributeMessage {
  public static RemoveAttribute from(final Node node, final AttributeSet set, final TrackedAttribute tracked) {
    return new RemoveAttribute(node, set, tracked);
  }
  
  public RemoveAttribute(final Node node, final AttributeSet set, final TrackedAttribute tracked) {
    super(node, set, tracked, ApplicationMessageType.RemoveAttribute);
  }
}

// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute.message;

import io.vlingo.wire.node.Node;

public abstract class ApplicationMessage {
  public static final String NoCorrelatingMessageId = "-";
  
  public final String correlatingMessageId;
  public final String trackingId;
  public final ApplicationMessageType type;
  
  public static String trackingId(final Node node, final ApplicationMessageType type, final String trailingId) {
    return node.name().value() + ":" + type.name() + ":" + trailingId;
  }
  
  protected ApplicationMessage(final String correlatingMessageId, final ApplicationMessageType type, final String trackingId) {
    this.correlatingMessageId = correlatingMessageId;
    this.trackingId = trackingId;
    this.type = type;
  }

  public abstract String toPayload();

  @Override
  public String toString() {
    return toPayload();
  }
}

// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import io.vlingo.xoom.wire.node.Id;

public final class Leave extends OperationalMessage {
  public static Leave from(final String content) {
    return new Leave(OperationalMessagePartsBuilder.idFrom(content));
  }

  public Leave(final Id id) {
    super(id);
  }

  @Override
  public boolean isLeave() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Leave.class) {
      return false;
    }

    return this.id.equals(((Leave) other).id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode();
  }

  @Override
  public String toString() {
    return "Leave[" + id + "]";
  }
}

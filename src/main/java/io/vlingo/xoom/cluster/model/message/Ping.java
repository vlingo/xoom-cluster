// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import io.vlingo.xoom.wire.node.Id;

public final class Ping extends OperationalMessage {
  public static Ping from(final String content) {
    return new Ping(OperationalMessagePartsBuilder.idFrom(content));
  }

  public Ping(final Id id) {
    super(id);
  }

  @Override
  public boolean isPing() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Ping.class) {
      return false;
    }

    return this.id.equals(((Ping) other).id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode();
  }

  @Override
  public String toString() {
    return "Ping[" + id + "]";
  }
}

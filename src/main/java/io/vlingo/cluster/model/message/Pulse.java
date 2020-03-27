// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import io.vlingo.wire.node.Id;

public final class Pulse extends OperationalMessage {
  public static Pulse from(final String content) {
    return new Pulse(OperationalMessagePartsBuilder.idFrom(content));
  }

  public Pulse(final Id id) {
    super(id);
  }

  @Override
  public boolean isPulse() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Pulse.class) {
      return false;
    }

    return this.id.equals(((Pulse) other).id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode();
  }

  @Override
  public String toString() {
    return "Pulse[" + id + "]";
  }
}

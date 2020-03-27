// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import io.vlingo.wire.node.Id;

public final class Split extends OperationalMessage {
  public static Split from(final String content) {
    return new Split(OperationalMessagePartsBuilder.idFrom(content));
  }

  public Split(final Id id) {
    super(id);
  }

  @Override
  public boolean isSplit() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Split.class) {
      return false;
    }

    return this.id.equals(((Split) other).id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode();
  }

  @Override
  public String toString() {
    return "Split[" + id + "]";
  }
}

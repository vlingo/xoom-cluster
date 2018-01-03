// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import io.vlingo.cluster.model.node.Id;

public final class Elect extends OperationalMessage {
  public static final Elect from(final String content) {
    return new Elect(OperationalMessagePartsBuilder.idFrom(content));
  }

  public Elect(final Id id) {
    super(id);
  }

  @Override
  public boolean isElect() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Elect.class) {
      return false;
    }

    return this.id.equals(((Elect) other).id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode();
  }

  @Override
  public String toString() {
    return "Elect[" + id + "]";
  }
}

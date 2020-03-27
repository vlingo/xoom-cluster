// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import io.vlingo.wire.node.Id;

public final class Vote extends OperationalMessage {
  public static Vote from(final String content) {
    return new Vote(OperationalMessagePartsBuilder.idFrom(content));
  }

  public Vote(final Id id) {
    super(id);
  }

  @Override
  public boolean isVote() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Vote.class) {
      return false;
    }

    return this.id.equals(((Vote) other).id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode();
  }

  @Override
  public String toString() {
    return "Vote[" + id + "]";
  }
}

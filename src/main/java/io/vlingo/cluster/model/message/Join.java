// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import io.vlingo.wire.node.Node;

public final class Join extends OperationalMessage {
  private final Node node;

  public static final Join from(final String content) {
    return new Join(OperationalMessagePartsBuilder.nodeFrom(content));
  }

  public Join(final Node node) {
    super(node.id());

    this.node = node;
  }

  @Override
  public boolean isJoin() {
    return true;
  }

  public Node node() {
    return node;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Join.class) {
      return false;
    }

    Join join = (Join) other;

    return this.node.equals(join.node);
  }

  @Override
  public int hashCode() {
    return 31 * node.hashCode();
  }

  @Override
  public String toString() {
    return "Join[" + node + "]";
  }
}

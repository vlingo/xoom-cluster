// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import io.vlingo.wire.node.Node;

public class MergeResult implements Comparable<MergeResult> {
  private final boolean joined;
  private final Node node;

  public MergeResult(final Node node, final boolean joined) {
    this.node = node;
    this.joined = joined;
  }

  public final boolean joined() {
    return joined;
  }

  public boolean left() {
    return !joined;
  }

  public final Node node() {
    return node;
  }

  public int compareTo(MergeResult other) {
    return this.node.compareTo(other.node);
  }
}

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public final class Directory extends OperationalMessage {
  private final Name name;
  private final Set<Node> nodes;

  public static final Directory from(final String content) {
    final Id id = OperationalMessagePartsBuilder.idFrom(content);
    final Name name = OperationalMessagePartsBuilder.nameFrom(content);
    final Set<Node> nodes = OperationalMessagePartsBuilder.nodesFrom(content);

    return new Directory(id, name, nodes);
  }

  public Directory(final Id id, final Name name, final Set<Node> nodes) {
    super(id);

    this.name = name;
    this.nodes = sorted(nodes);
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  public boolean isValid() {
    for (final Node node : nodes) {
      if (!node.isValid()) {
        return false;
      }
    }
    return true;
  }

  public final Set<Node> nodes() {
    return Collections.unmodifiableSet(nodes);
  }

  public final Name name() {
    return name;
  }

  public int size() {
    return this.nodes.size();
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Directory.class) {
      return false;
    }

    return this.nodes.equals(((Directory) other).nodes);
  }

  @Override
  public int hashCode() {
    return 31 * nodes.hashCode();
  }

  @Override
  public String toString() {
    return "Directory[" + id() + "," + name + "," + nodes + "]";
  }

  private final Set<Node> sorted(Set<Node> nodes) {
    return new TreeSet<Node>(nodes);
  }
}

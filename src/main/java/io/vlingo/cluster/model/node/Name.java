// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

public final class Name {
  public static final String NO_NAME = "?";
  public static final Name NO_NODE_NAME = new Name(NO_NAME);

  private final String value;

  public Name(final String name) {
    this.value = name;
  }

  public final String value() {
    return value;
  }

  public boolean hasNoName() {
    return value().equals(NO_NAME);
  }

  public boolean sameAs(String name) {
    return value.equals(name);
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Name.class) {
      return false;
    }

    return this.value.equals(((Name) other).value);
  }

  @Override
  public int hashCode() {
    return 31 * value.hashCode();
  }

  @Override
  public String toString() {
    return "Name[" + value + "]";
  }
}

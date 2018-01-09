// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

public final class Id implements Comparable<Id> {
  public static final short UNDEFINED_ID = -1;
  public static final Id NO_ID = Id.of(UNDEFINED_ID);

  private final short value;

  public static Id of(final int id) {
    return new Id(id);
  }

  public static Id of(final short id) {
    return new Id(id);
  }

  public Id(final short id) {
    this.value = id;
  }

  public Id(final int id) {
    this((short) id);
  }

  public boolean hasNoId() {
    return value == UNDEFINED_ID;
  }

  public short value() {
    return value;
  }

  public String valueString() {
    return String.valueOf(value);
  }

  public boolean isValid() {
    return !hasNoId();
  }

  public int toInteger() {
    return (int) value;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Id.class) {
      return false;
    }

    return this.value == ((Id) other).value;
  }

  @Override
  public int hashCode() {
    return 31 * value;
  }

  @Override
  public String toString() {
    return "Id[" + value + "]";
  }

  public int compareTo(Id other) {
    return Short.compare(this.value, other.value);
  }

  public boolean greaterThan(final Id other) {
    return this.value > other.value;
  }
}

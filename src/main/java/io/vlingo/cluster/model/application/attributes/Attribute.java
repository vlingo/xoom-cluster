// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application.attributes;

public final class Attribute {
  public static enum Type {
    Byte,
    Short,
    Integer,
    Long,
    Character,
    Float,
    Double,
    Boolean,
    String
  }
  
  public final String name;
  public final Type type;
  public final String value;
  
  public Attribute(final String name, final String value, final Type type) {
    this.name = name;
    this.value = value;
    this.type = type;
  }

  public byte toByteValue() {
    return (byte) Integer.parseInt(value);
  }

  public short toShortValue() {
    return Short.parseShort(value);
  }

  public int toIntegerValue() {
    return Integer.parseInt(value);
  }

  public long toLongValue() {
    return Long.parseLong(value);
  }

  public char toCharacterValue() {
    return value.charAt(0);
  }

  public float toFloatValue() {
    return Float.parseFloat(value);
  }

  public double toDoubleValue() {
    return Double.parseDouble(value);
  }

  public boolean toBooleanValue() {
    return Boolean.parseBoolean(value);
  }

  public String toStringValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return 31 * this.name.hashCode() + this.value.hashCode() + this.type.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != Attribute.class) {
      return false;
    }
    
    Attribute otherAttribute = (Attribute) other;
    
    return this.name.equals(otherAttribute.name) &&
            this.value.equals(otherAttribute.value) &&
            this.type.ordinal() == otherAttribute.type.ordinal();
  }

  @Override
  public String toString() {
    return "Attribute[name=" + this.name + ", value=" + this.value + ", type" + this.type + "]";
  }
}

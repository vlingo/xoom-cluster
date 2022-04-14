// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

public final class Attribute<T> {
  public enum Type {
    Byte { @Override public boolean isByte() { return true; } },
    Short { @Override public boolean isShort() { return true; } },
    Integer { @Override public boolean isInteger() { return true; } },
    Long { @Override public boolean isLong() { return true; } },
    Character { @Override public boolean isCharacter() { return true; } },
    Float { @Override public boolean isFloat() { return true; } },
    Double { @Override public boolean isDouble() { return true; } },
    Boolean { @Override public boolean isBoolean() { return true; } },
    String { @Override public boolean isString() { return true; } };
    
    public boolean isByte() { return false; }
    public boolean isShort() { return false; }
    public boolean isInteger() { return false; }
    public boolean isLong() { return false; }
    public boolean isCharacter() { return false; }
    public boolean isFloat() { return false; }
    public boolean isDouble() { return false; }
    public boolean isBoolean() { return false; }
    public boolean isString() { return false; }
    
    public String typeClassname() { return "java.lang." + this.name(); }
  }
  
  public static final Attribute<?> Undefined = from("__undefined", Type.String, ""); 
  
  public static <T> Attribute<T> from(final String name, final T value) {
    return new Attribute<>(name, value, typeOf(value.getClass()));
  }
  
  public static Attribute<?> from(final String name, final Type type, final String value) {
    final Object typedValue = typeValue(type, value);
    return new Attribute<>(name, typedValue, type);
  }

  static Type typeOfAttribute(final String classnameOfType) {
    try {
      final Class<?> classOfType = Class.forName(classnameOfType);
      return typeOf(classOfType);
    } catch (Exception e) {
      throw new IllegalArgumentException("The type '" + classnameOfType + "' is not recognized.");
    }
  }
  
  private static Type typeOf(final Class<?> type) {
    switch (type.getName()) {
    case "java.lang.String":
      return Type.String;
    case "int":
    case "java.lang.Integer":
      return Type.Integer;
    case "long":
    case "java.lang.Long":
      return Type.Long;
    case "boolean":
    case "java.lang.Boolean":
      return Type.Boolean;
    case "byte":
    case "java.lang.Byte":
      return Type.Byte;
    case "double":
    case "java.lang.Double":
      return Type.Double;
    case "float":
    case "java.lang.Float":
      return Type.Float;
    case "short":
    case "java.lang.Short":
      return Type.Short;
    case "char":
    case "java.lang.Character":
      return Type.Character;
    }
    
    throw new IllegalArgumentException("The type '" + type.getName() + "' is not recognized.");
  }
  
  private static Object typeValue(final Type type, String value) {
    switch (type) {
    case String:
      return value;
    case Integer:
      return Integer.parseInt(value);
    case Long:
      return Long.parseLong(value);
    case Boolean:
      return Boolean.parseBoolean(value);
    case Byte:
      return Byte.parseByte(value);
    case Double:
      return Double.parseDouble(value);
    case Float:
      return Float.parseFloat(value);
    case Short:
      return Short.parseShort(value);
    case Character:
      return value.charAt(0);
    }
    
    throw new IllegalArgumentException();
  }

  public final String name;
  public final Type type;
  public final T value;
  
  public Attribute(final String name, final T value, final Type type) {
    this.name = name;
    this.value = value;
    this.type = type;
  }

  public boolean isUndefined() {
    return this == Undefined;
  }

  public Attribute<?> replacingValueWith(final Attribute<?> other) {
    if (this.type != other.type) {
      throw new IllegalArgumentException("Source and target attributes have different types.");
    }
    return new Attribute<>(this.name, other.value, this.type);
  }

  @Override
  public int hashCode() {
    return 31 * this.name.hashCode() + this.value.hashCode() + this.type.hashCode();
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != Attribute.class) {
      return false;
    }
    
    Attribute<T> otherAttribute = (Attribute<T>) other;
    
    return this.name.equals(otherAttribute.name) &&
            this.value.equals(otherAttribute.value) &&
            this.type.ordinal() == otherAttribute.type.ordinal();
  }

  @Override
  public String toString() {
    return "Attribute[name=" + this.name + ", value=" + this.value + ", type=" + this.type + "]";
  }
}

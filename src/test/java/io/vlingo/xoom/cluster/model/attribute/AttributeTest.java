// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.vlingo.xoom.cluster.model.attribute.Attribute.Type;

public class AttributeTest {

  @Test
  public void testByteValue() {
    final Attribute<Byte> attribute1 = Attribute.from("byte1", (byte) 1);
    
    assertEquals((byte) 1, (byte) attribute1.value);
    
    assertEquals(Byte.valueOf((byte) 1), (Byte) attribute1.value);
    
    assertTrue(attribute1.type.isByte());
    
    final Attribute<Byte> attribute2 = Attribute.from("byte2", Byte.valueOf((byte) 2));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("byte2", Byte.valueOf((byte) 2)));
  }

  @Test
  public void testShortValue() {
    final Attribute<Short> attribute1 = Attribute.from("short1", (short) 1);
    
    assertEquals((short) 1, (short) attribute1.value);
    
    assertEquals(Short.valueOf((short) 1), (Short) attribute1.value);
    
    assertTrue(attribute1.type.isShort());
    
    final Attribute<Short> attribute2 = Attribute.from("short2", Short.valueOf((short) 2));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("short2", Short.valueOf((short) 2)));
  }

  @Test
  public void testIntegerValue() {
    final Attribute<Integer> attribute1 = Attribute.from("int1", 1);
    
    assertEquals(1, (int) attribute1.value);
    
    assertEquals(Integer.valueOf(1), (Integer) attribute1.value);
    
    assertTrue(attribute1.type.isInteger());
    
    final Attribute<Integer> attribute2 = Attribute.from("int2", Integer.valueOf(2));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("int2", Integer.valueOf(2)));
  }

  @Test
  public void testLongValue() {
    final Attribute<Long> attribute1 = Attribute.from("long1", 1L);
    
    assertEquals(1L, (long) attribute1.value);
    
    assertEquals(Long.valueOf(1), (Long) attribute1.value);
    
    assertTrue(attribute1.type.isLong());
    
    final Attribute<Long> attribute2 = Attribute.from("long2", Long.valueOf(2L));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("long2", Long.valueOf(2L)));
  }

  @Test
  public void testCharacterValue() {
    final Attribute<Character> attribute1 = Attribute.from("char1", 'A');
    
    assertEquals('A', (long) attribute1.value);
    
    assertEquals(Character.valueOf('A'), (Character) attribute1.value);
    
    assertTrue(attribute1.type.isCharacter());
    
    final Attribute<Character> attribute2 = Attribute.from("char2", Character.valueOf('B'));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("char2", Character.valueOf('B')));
  }

  @Test
  public void testFloatValue() {
    final Attribute<Float> attribute1 = Attribute.from("float1", 1.1f);
    
    assertEquals(1.1f, (float) attribute1.value, 0.05);
    
    assertEquals(Float.valueOf(1.1F), (Float) attribute1.value);
    
    assertTrue(attribute1.type.isFloat());
    
    final Attribute<Float> attribute2 = Attribute.from("float2", Float.valueOf(2.2F));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("float2", Float.valueOf(2.2F)));
  }

  @Test
  public void testDoubleValue() {
    final Attribute<Double> attribute1 = Attribute.from("double1", 1.1);
    
    assertEquals(1.1, (double) attribute1.value, 0.05);
    
    assertEquals(Double.valueOf(1.1), (Double) attribute1.value);
    
    assertTrue(attribute1.type.isDouble());
    
    final Attribute<Double> attribute2 = Attribute.from("double2", Double.valueOf(2.2));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("double2", Double.valueOf(2.2)));
  }

  @Test
  public void testBooleanValue() {
    final Attribute<Boolean> attribute1 = Attribute.from("boolean1", true);
    
    assertEquals(true, (boolean) attribute1.value);
    
    assertEquals(Boolean.valueOf(true), (Boolean) attribute1.value);
    
    assertTrue(attribute1.type.isBoolean());
    
    final Attribute<Boolean> attribute2 = Attribute.from("boolean2", Boolean.valueOf(false));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("boolean2", Boolean.valueOf(false)));
  }

  @Test
  public void testStringValue() {
    final Attribute<String> attribute1 = Attribute.from("string1", "A");
    
    assertEquals("A", (String) attribute1.value);
    
    assertEquals(new String("A"), (String) attribute1.value);
    
    assertTrue(attribute1.type.isString());
    
    final Attribute<String> attribute2 = Attribute.from("string2", new String("B"));
    
    assertNotEquals(attribute1, attribute2);
    
    assertEquals(attribute2, Attribute.from("string2", new String("B")));
  }

  @Test
  public void testFromAttributeType() {
    final Attribute<?> attribute1 = Attribute.from("int1", Type.Integer, "101");
    
    assertEquals(101, attribute1.value);
  }
}

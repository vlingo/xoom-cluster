// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

public final class TrackedAttribute {
  public static final TrackedAttribute Absent = new TrackedAttribute(null, null);
  
  public final Attribute<?> attribute;
  public final boolean distributed;
  public final String id;

  static TrackedAttribute of(final AttributeSet set, final Attribute<?> attribute) {
    final String tid = trackedIdFor(set, attribute);
    return new TrackedAttribute(tid, attribute);
  }
  
  private static String trackedIdFor(final AttributeSet set, final Attribute<?> attribute) {
    return set.name + ":" + attribute.name;
  }
  
  public TrackedAttribute asDistributed() {
    return new TrackedAttribute(this.id, this.attribute, true);
  }
  
  public boolean isAbsent() {
    return this.attribute == null;
  }
  
  public boolean isDistributed() {
    return distributed;
  }
  
  public boolean isPresent() {
    return !isAbsent();
  }

  public Attribute<?> replacingValueWith(final Attribute<?> other) {
    return attribute.replacingValueWith(other);
  }

  public boolean sameAs(final Attribute<?> other) {
    return attribute.equals(other);
  }

  public TrackedAttribute withAttribute(final Attribute<?> attribute) {
    return new TrackedAttribute(this.id, attribute, false);
  }

  @Override
  public int hashCode() {
    return 31 * this.attribute.hashCode() + Boolean.hashCode(this.distributed) + this.id.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != TrackedAttribute.class) {
      return false;
    }
    
    TrackedAttribute otherTracked = (TrackedAttribute) other;
    
    return this.attribute.equals(otherTracked.attribute) &&
            this.distributed == otherTracked.distributed &&
            this.id.equals(otherTracked.id);
  }

  @Override
  public String toString() {
    return "TrackedAttribute[attribute=" + this.attribute + ", distributed=" + this.distributed + ", id=" + this.id + "]";
  }

  private TrackedAttribute(final String id, final Attribute<?> attribute) {
    this.attribute = attribute;
    this.distributed = false;
    this.id = attribute == null ? null : id;
  }
  
  private TrackedAttribute(final String id, final Attribute<?> attribute, final boolean distributed) {
    this.attribute = attribute;
    this.distributed = distributed;
    this.id = id;
  }
}

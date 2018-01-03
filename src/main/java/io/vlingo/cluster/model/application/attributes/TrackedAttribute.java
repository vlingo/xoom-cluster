// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application.attributes;

import java.util.UUID;

final class TrackedAttribute {
  protected static final TrackedAttribute Absent = new TrackedAttribute(null);
  
  protected final Attribute<?> attribute;
  protected final boolean distributed;
  protected final String id;
  
  protected static TrackedAttribute of(final Attribute<?> attribute) {
    return new TrackedAttribute(attribute);
  }
  
  protected TrackedAttribute asDistributed() {
    return new TrackedAttribute(this.id, this.attribute, true);
  }
  
  protected boolean isAbsent() {
    return this.attribute == null;
  }
  
  protected boolean isPresent() {
    return !isAbsent();
  }
  
  protected TrackedAttribute withAttribute(final Attribute<?> attribute) {
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
    return "Attribute[attribute=" + this.attribute + ", distributed=" + this.distributed + ", id=" + this.id + "]";
  }

  private TrackedAttribute(final Attribute<?> attribute) {
    this.attribute = attribute;
    this.distributed = false;
    this.id = attribute == null ? null : UUID.randomUUID().toString();
  }
  
  private TrackedAttribute(final String id, final Attribute<?> attribute, final boolean distributed) {
    this.attribute = attribute;
    this.distributed = distributed;
    this.id = id;
  }
}

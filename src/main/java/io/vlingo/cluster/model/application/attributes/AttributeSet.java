// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application.attributes;

import java.util.HashMap;
import java.util.Map;

public final class AttributeSet {
  public final String name;
  private final Map<String, TrackedAttribute> attributes;
  
  protected static AttributeSet named(final String name) {
    return new AttributeSet(name);
  }
  
  protected TrackedAttribute addIfAbsent(final Attribute attribute) {
    final TrackedAttribute maybeAttribute = find(attribute);
    
    if (maybeAttribute.isAbsent()) {
      final TrackedAttribute nowPresent = TrackedAttribute.of(attribute);
      
      attributes.put(nowPresent.id, nowPresent);
      
      return nowPresent;
    }
    
    return maybeAttribute;
  }
  
  protected TrackedAttribute attributeNamed(final String name) {
    return find(name);
  }
  
  protected TrackedAttribute remove(final Attribute attribute) {
    final TrackedAttribute maybeAttribute = find(attribute);
    
    if (maybeAttribute.isPresent()) {
      return attributes.remove(maybeAttribute.id);
    }
    
    return maybeAttribute;
  }
  
  protected TrackedAttribute replace(final Attribute attribute) {
    final TrackedAttribute maybeAttribute = find(attribute);
    
    if (maybeAttribute.isPresent()) {
      return attributes.remove(maybeAttribute.id);
    }
    
    return maybeAttribute;
  }
  
  private AttributeSet(final String name) {
    this.name = name;
    this.attributes = new HashMap<>();
  }
  
  private TrackedAttribute find(final Attribute attribute) {
    return find(attribute.name);
  }
  
  private TrackedAttribute find(final String name) {
    for (final TrackedAttribute id : attributes.values()) {
      if (id.attribute.name.equals(name)) {
        return id;
      }
    }
    
    return TrackedAttribute.absent;
  }
}

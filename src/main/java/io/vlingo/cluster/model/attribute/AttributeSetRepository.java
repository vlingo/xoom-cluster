// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AttributeSetRepository {
  private final Map<String,AttributeSet> all;
  
  protected AttributeSetRepository() {
    this.all = new ConcurrentHashMap<>(16, 0.75f, 16);
  }

  protected void add(final AttributeSet set) {
    all.put(set.name, set);
  }

  protected Collection<AttributeSet> all() {
    return new ArrayList<>(all.values());
  }

  protected AttributeSet attributeSetOf(final String name) {
    return all.getOrDefault(name, AttributeSet.None);
  }

  protected void remove(final String name) {
    all.remove(name);
  }

  protected void removeAll() {
    all.clear();
  }
}

// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AttributeSetRepository {
  private final Map<String,AttributeSet> all;
  
  AttributeSetRepository() {
    this.all = new ConcurrentHashMap<>(16, 0.75f, 16);
  }

  void add(final AttributeSet set) {
    all.put(set.name, set);
  }

  Collection<AttributeSet> all() {
    return new ArrayList<>(all.values());
  }

  AttributeSet attributeSetOf(final String name) {
    return all.getOrDefault(name, AttributeSet.None);
  }

  void remove(final String name) {
    all.remove(name);
  }

  void removeAll() {
    all.clear();
  }

  void syncWith(final AttributeSet set) {
    all.put(set.name, set);
  }
}

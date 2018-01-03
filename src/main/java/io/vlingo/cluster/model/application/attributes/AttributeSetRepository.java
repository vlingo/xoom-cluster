// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application.attributes;

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

  protected AttributeSet attributeSetOf(final String name) {
    return all.get(name);
  }

  protected void remove(final String name) {
    all.remove(name);
  }
}

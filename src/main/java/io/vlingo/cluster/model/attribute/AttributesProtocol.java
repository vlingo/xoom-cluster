// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import java.util.Collection;

public interface AttributesProtocol {
  <T> void add(final String attributeSetName, final String attributeName, final T value);
  Collection<AttributeSet> all();
  Collection<Attribute<?>> allOf(final String attributeSetName);
  <T> Attribute<T> attribute(final String attributeSetName, final String attributeName);
  <T> void replace(final String attributeSetName, final String attributeName, final T value);
  <T> void remove(final String attributeSetName, final String attributeName);
}

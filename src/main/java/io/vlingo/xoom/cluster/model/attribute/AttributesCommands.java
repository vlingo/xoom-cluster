// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute;

public interface AttributesCommands {
  <T> void add(final String attributeSetName, final String attributeName, final T value);
  <T> void replace(final String attributeSetName, final String attributeName, final T value);
  <T> void remove(final String attributeSetName, final String attributeName);
  <T> void removeAll(final String attributeSetName);
}

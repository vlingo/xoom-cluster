// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class AttributesClient implements AttributesProtocol {
  final AttributesAgent agent;
  final AttributeSetRepository repository;
  
  static synchronized AttributesClient with(final AttributesAgent agent) {
    return new AttributesClient(agent, new AttributeSetRepository());
  }

  @Override
  public <T> void add(final String attributeSetName, final String attributeName, final T value) {
    agent.add(attributeSetName, attributeName, value);
  }
  
  @Override
  public Collection<AttributeSet> all() {
    return repository.all();
  }

  @Override
  public Collection<Attribute<?>> allOf(final String attributeSetName) {
    final List<Attribute<?>> all = new ArrayList<>();
    final AttributeSet set = repository.attributeSetOf(attributeSetName);
    if (set.isDefined()) {
      for (final TrackedAttribute tracked : set.all()) {
        if (tracked.isPresent()) {
          all.add(tracked.attribute);
        }
      }
    }
    return all;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Attribute<T> attribute(final String attributeSetName, final String attributeName) {
    final AttributeSet set = repository.attributeSetOf(attributeSetName);
    if (set.isDefined()) {
      final TrackedAttribute tracked = set.attributeNamed(attributeName);
      if (tracked.isPresent()) {
        return (Attribute<T>) tracked.attribute;
      }
    }
    return (Attribute<T>) Attribute.Undefined;
  }

  @Override
  public <T> void replace(final String attributeSetName, final String attributeName, final T value) {
    agent.replace(attributeSetName, attributeName, value);
  }

  @Override
  public <T> void remove(final String attributeSetName, final String attributeName) {
    agent.remove(attributeSetName, attributeName);
  }

  @Override
  public <T> void removeAll(final String attributeSetName) {
    agent.removeAll(attributeSetName);
  }

  @Override
  public String toString() {
    return "AttributesClient[agent=" + agent + " repository=" + repository + "]";
  }

  void syncWith(final AttributeSet set) {
    repository.syncWith(set.copy(set));
  }

  void syncWithout(final AttributeSet set) {
    repository.remove(set.name);
  }

  private AttributesClient(final AttributesAgent agent, final AttributeSetRepository repository) {
    this.agent = agent;
    this.repository = repository;
  }
}

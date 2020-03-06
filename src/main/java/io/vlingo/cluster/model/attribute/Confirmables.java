// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import java.util.*;

import io.vlingo.cluster.model.Properties;
import io.vlingo.cluster.model.attribute.message.ApplicationMessage;
import io.vlingo.wire.node.Node;

final class Confirmables {
  private final Collection<Node> allOtherNodes;
  private final List<Confirmable> expectedConfirmables;
  private final Node node;

  Confirmables(final Node node, final Collection<Node> allOtherNodes) {
    this.node = node;
    this.allOtherNodes = allOtherNodes;
    this.expectedConfirmables = new ArrayList<>();
  }

  Collection<Confirmable> allRedistributable() {
    final List<Confirmable> ready = new ArrayList<>();
    for (final Confirmable confirmable : expectedConfirmables) {
      if (confirmable.isRedistributableAsOf()) {
        ready.add(confirmable);
      }
    }
    return ready;
  }

  Collection<String> allTrackingIds() {
    final List<String> all = new ArrayList<>();
    
    for (final Confirmable confirmable : expectedConfirmables) {
      all.add(confirmable.message.trackingId);
    }
    
    return all;
  }

  void confirm(final String trackingId, final Node node) {
    final Confirmable confirmable = confirmableOf(trackingId);
    confirmable.confirm(node);
    if (!confirmable.hasUnconfirmedNodes()) {
      expectedConfirmables.remove(confirmable);
    }
  }

  Confirmable confirmableOf(final String trackingId) {
    for (final Confirmable confirmable : expectedConfirmables) {
      if (Objects.equals(confirmable.trackingId(), trackingId)) {
        return confirmable;
      }
    }
    return Confirmable.NoConfirmable;
  }

  Confirmable unconfirmed(final ApplicationMessage message) {
    return unconfirmedFor(message, allOtherNodes);
  }

  Confirmable unconfirmedFor(final ApplicationMessage message, final Collection<Node> nodes) {
    if (nodes.contains(node)) {
      new Exception().printStackTrace();
    }
    final Confirmable confirmable = new Confirmable(message, nodes);
    expectedConfirmables.add(confirmable);
    return confirmable;
  }

  static final class Confirmable {
    static final Confirmable NoConfirmable = new Confirmable();

    private final int clusterAttributesRedistributionRetries;
    private final long clusterAttributesRedistributionInterval;
    private final long createdOn;
    private final ApplicationMessage message;
    private Map<Node, Integer> unconfirmedNodes;
    
    Confirmable(final ApplicationMessage message, final Collection<Node> allOtherNodes) {
      this(message, allOtherNodes, System.currentTimeMillis(), Properties.instance);
    }

    Confirmable(final ApplicationMessage message,
                final Collection<Node> allOtherNodes,
                final long createdOn,
                final Properties clusterProperties) {
      this.message = message;
      this.unconfirmedNodes = allUnconfirmedFor(allOtherNodes);
      this.createdOn = createdOn;

      this.clusterAttributesRedistributionRetries =
          clusterProperties.clusterAttributesRedistributionRetries();
      this.clusterAttributesRedistributionInterval =
          clusterProperties.clusterAttributesRedistributionInterval();

    }

    private Confirmable() {
      this(null,
          Collections.emptyList(),
          System.currentTimeMillis(),
          Properties.instance);
    }

    private static String apply(ApplicationMessage m) {
      return m.trackingId;
    }

    private Map<Node, Integer> allUnconfirmedFor(final Collection<Node> allOtherNodes) {
      final Map<Node, Integer> allUnconfirmed =
          new HashMap<>(allOtherNodes.size(), 1);

      for (final Node node : allOtherNodes) {
        allUnconfirmed.put(node, 0);
      }
      return allUnconfirmed;
    }

    void confirm(final Node node) {
      unconfirmedNodes.remove(node);
    }

    boolean hasUnconfirmedNodes() {
      return !unconfirmedNodes.isEmpty();
    }

    ApplicationMessage message() {
      return message;
    }

    boolean isRedistributableAsOf() {
      final long targetTime = createdOn + clusterAttributesRedistributionInterval;
      if (targetTime < System.currentTimeMillis()) {
        final Map<Node, Integer> allUnconfirmed = new HashMap<>(unconfirmedNodes.size());
        
        for (final Node node : unconfirmedNodes.keySet()) {
          final int tries = unconfirmedNodes.get(node) + 1;
          if (tries <= clusterAttributesRedistributionRetries) {
            allUnconfirmed.put(node, tries);
          }
        }
        unconfirmedNodes = allUnconfirmed;
        
        return true;
      }
      return false;
    }

    Collection<Node> unconfirmedNodes() {
      return unconfirmedNodes.keySet();
    }
    
    @Override
    public boolean equals(final Object other) {
      if (other == null || other.getClass() != Confirmable.class) {
        return false;
      }

      return Objects.equals(this.trackingId(), ((Confirmable)other).trackingId());
    }

    String trackingId() {
      return (message != null) ? message.trackingId : null;
    }
    
    @Override
    public String toString() {
      return "Confirmable[trackingId=" + trackingId() + " nodes=" + unconfirmedNodes + "]";
    }
  }
}

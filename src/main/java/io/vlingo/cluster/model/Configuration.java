// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.Collection;
import java.util.Set;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Node;

public interface Configuration {
  Collection<Node> allConfiguredNodes();
  Collection<Node> allGreaterConfiguredNodes(final Id id);
  Set<Node> allOtherConfiguredNodes(final Id id);
  Collection<String> allConfiguredNodeNames();
  Node configuredNodeMatching(final Id id);
  Id greatestConfiguredNodeId();
  boolean hasConfiguredNode(final Id id);
  int totalConfiguredNodes();
}

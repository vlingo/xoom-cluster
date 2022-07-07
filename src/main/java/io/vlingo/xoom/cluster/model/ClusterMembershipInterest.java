// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.wire.node.Node;

interface ClusterMembershipInterest {
  void nodeAdded(Node node, boolean isClusterHealthy);
  void nodeLeft(Node node, boolean isClusterHealthy);
  void informClusterIsHealthy(boolean isClusterHealthy);
}
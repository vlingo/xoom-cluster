// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.wire.node.Id;

/**
 * This class models a node which is at the same time a scalecube-cluster seed.
 */
public class SeedNode {
  public final Id id;
  public final String operationalHost;
  public final int operationalPort;

  public SeedNode(Id id, String operationalHost, int operationalPort) {
    this.id = id;
    this.operationalHost = operationalHost;
    this.operationalPort = operationalPort;
  }
}

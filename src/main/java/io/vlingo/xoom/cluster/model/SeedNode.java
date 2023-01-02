// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

/**
 * This class models a node which is at the same time a scalecube-cluster seed.
 */
public class SeedNode {

  public static SeedNode from(String seedNodeString) {
    String[] seedNodeValues = seedNodeString.split(":");
    if (seedNodeValues.length != 2) {
      throw new IllegalArgumentException("Invalid seed node properties " + seedNodeString + ". Expected format 'host:port'");
    }

    int operationalPort = Integer.parseInt(seedNodeValues[1]);
    return new SeedNode(seedNodeValues[0], operationalPort);
  }

  public final String operationalHost;
  public final int operationalPort;

  public SeedNode(String operationalHost, int operationalPort) {
    this.operationalHost = operationalHost;
    this.operationalPort = operationalPort;
  }
}

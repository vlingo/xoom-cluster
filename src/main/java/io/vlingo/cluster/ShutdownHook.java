// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster;

import io.vlingo.cluster.model.ClusterSnapshotControl;

final class ShutdownHook {
  private final ClusterSnapshotControl control;
  private final String nodeName;

  protected ShutdownHook(final String nodeName, final ClusterSnapshotControl control) {
    this.nodeName = nodeName;
    this.control = control;
  }

  protected void register() throws Exception {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.out.println("\n==========");
        System.out.println("vlingo/cluster: Stopping node: '" + nodeName + "' ...");
        control.shutDown();
        pause();
        System.out.println("vlingo/cluster: Stopped node: '" + nodeName + "'");
      }
    });
  }
  
  private void pause() {
    try {
      Thread.sleep(1000L);
    } catch (Exception e) {
      // ignore
    }
  }
}

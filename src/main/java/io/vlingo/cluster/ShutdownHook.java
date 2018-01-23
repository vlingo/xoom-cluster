// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster;

import io.vlingo.actors.Logger;
import io.vlingo.cluster.model.ClusterSnapshotControl;
import io.vlingo.common.fn.Tuple2;

final class ShutdownHook {
  private final Tuple2<ClusterSnapshotControl, Logger> control;
  private final String nodeName;

  protected ShutdownHook(final String nodeName, final Tuple2<ClusterSnapshotControl, Logger> control) {
    this.nodeName = nodeName;
    this.control = control;
  }

  protected void register() throws Exception {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        control._2.log("\n==========");
        control._2.log("vlingo/cluster: Stopping node: '" + nodeName + "' ...");
        control._1.shutDown();
        pause();
        control._2.log("vlingo/cluster: Stopped node: '" + nodeName + "'");
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

// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Logger;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.application.ClusterApplication;
import io.vlingo.common.fn.Tuple2;

public interface ClusterSnapshotControl {
  public static Tuple2<ClusterSnapshotControl, Logger> instance(final World world, final String name) {
    final ClusterSnapshotInitializer initializer = new ClusterSnapshotInitializer(name, Properties.instance, world.defaultLogger());
    
    final ClusterApplication application = ClusterApplication.instance(world, initializer.localNode());
    
    final Definition definition =
            new Definition(
                    ClusterSnapshotActor.class,
                    Definition.parameters(initializer, application),
                    "cluster-snapshot-" + name);
    
    ClusterSnapshotControl control = world.actorFor(definition, ClusterSnapshotControl.class);
    
    return Tuple2.from(control, world.defaultLogger());
  }

  void shutDown();
}

// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.application.ClusterApplication;

public interface ClusterSnapshotControl {
  public static ClusterSnapshotControl instance(final World world, final String name) {
    final ClusterSnapshotInitializer initializer = new ClusterSnapshotInitializer(name, Properties.instance);
    
    final ClusterApplication application = ClusterApplication.instance(world, initializer.localNode());
    
    final Definition definition =
            new Definition(
                    ClusterSnapshotActor.class,
                    Definition.parameters(initializer, application),
                    "cluster-snapshot-" + name);
    
    ClusterSnapshotControl control = world.actorFor(definition, ClusterSnapshotControl.class);
    
    return control;
  }

  void shutDown();
}

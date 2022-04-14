// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.wire.node.Id;

public class ClusterApplicationBroadcasterTest extends AbstractClusterTest {
  private ClusterApplicationBroadcaster broadcaster;

  @Test
  public void testInformAllLiveNodes() throws Exception {
    broadcaster.informAllLiveNodes(config.allNodes(), true);
    assertEquals(1, application.allLiveNodes.get());
  }

  @Test
  public void testInformLeaderElected() throws Exception {
    broadcaster.informLeaderElected(Id.of(3), true, false);
    assertEquals(1, application.informLeaderElected.get());
  }

  @Test
  public void testInformLeaderLost() throws Exception {
    broadcaster.informLeaderLost(Id.of(3), false);
    assertEquals(1, application.informLeaderLost.get());
  }

  @Test
  public void testInformLocalNodeShutDown() throws Exception {
    broadcaster.informLocalNodeShutDown(Id.of(1));
    assertEquals(1, application.informLocalNodeShutDown.get());
  }

  @Test
  public void testInformLocalNodeStarted() throws Exception {
    broadcaster.informLocalNodeStarted(Id.of(1));
    assertEquals(1, application.informLocalNodeStarted.get());
  }

  @Test
  public void testInformNodeIsHealthy() throws Exception {
    broadcaster.informNodeIsHealthy(Id.of(1), true);
    assertEquals(1, application.informNodeIsHealthy.get());
  }

  @Test
  public void testInformNodeJoinedCluster() throws Exception {
    broadcaster.informNodeJoinedCluster(Id.of(2), true);
    assertEquals(1, application.informNodeJoinedCluster.get());
  }

  @Test
  public void testInformNodeLeftCluster() throws Exception {
    broadcaster.informNodeLeftCluster(Id.of(2), true);
    assertEquals(1, application.informNodeLeftCluster.get());
  }

  @Test
  public void testInformQuorumAchieved() throws Exception {
    broadcaster.informQuorumAchieved();
    assertEquals(1, application.informQuorumAchieved.get());
  }

  @Test
  public void testInformQuorumLost() throws Exception {
    broadcaster.informQuorumLost();
    assertEquals(1, application.informQuorumLost.get());
  }

  @Test
  public void testInformResponder() throws Exception {
    broadcaster.informResponder(null); // production must not be null
    assertEquals(1, application.informResponder.get());
  }

  @Test
  public void testInformAttributeSetCreated() throws Exception {
    broadcaster.informAttributeSetCreated("test");
    assertEquals(1, application.informAttributeSetCreated.get());
  }

  @Test
  public void testInformAttributeAdded() throws Exception {
    broadcaster.informAttributeAdded("test", "test");
    assertEquals(1, application.informAttributeAdded.get());
  }

  @Test
  public void testInformAttributeRemoved() throws Exception {
    broadcaster.informAttributeRemoved("test", "test");
    assertEquals(1, application.informAttributeRemoved.get());
  }

  @Test
  public void testInformAttributeReplaced() throws Exception {
    broadcaster.informAttributeReplaced("test", "test");
    assertEquals(1, application.informAttributeReplaced.get());
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    broadcaster = new ClusterApplicationBroadcaster(testWorld.defaultLogger());
    broadcaster.registerClusterApplication(application);
  }
}

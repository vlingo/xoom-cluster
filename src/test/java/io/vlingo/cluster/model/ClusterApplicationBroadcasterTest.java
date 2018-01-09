// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.cluster.model.node.Id;

public class ClusterApplicationBroadcasterTest extends AbstractClusterTest {
  private ClusterApplicationBroadcaster broadcaster;

  @Test
  public void testInformAllLiveNodes() throws Exception {
    final List<Id> ids = config.allConfiguredNodes().stream().map(node -> node.id()).collect(Collectors.toList());
    broadcaster.informAllLiveNodes(ids, true);
    assertEquals(1, application.allLiveNodes);
  }

  @Test
  public void testInformLeaderElected() throws Exception {
    broadcaster.informLeaderElected(Id.of(3), true, false);
    assertEquals(1, application.informLeaderElected);
  }

  @Test
  public void testInformLeaderLost() throws Exception {
    broadcaster.informLeaderLost(Id.of(3), false);
    assertEquals(1, application.informLeaderLost);
  }

  @Test
  public void testInformLocalNodeShutDown() throws Exception {
    broadcaster.informLocalNodeShutDown(Id.of(1));
    assertEquals(1, application.informLocalNodeShutDown);
  }

  @Test
  public void testInformLocalNodeStarted() throws Exception {
    broadcaster.informLocalNodeStarted(Id.of(1));
    assertEquals(1, application.informLocalNodeStarted);
  }

  @Test
  public void testInformNodeIsHealthy() throws Exception {
    broadcaster.informNodeIsHealthy(Id.of(1), true);
    assertEquals(1, application.informNodeIsHealthy);
  }

  @Test
  public void testInformNodeJoinedCluster() throws Exception {
    broadcaster.informNodeJoinedCluster(Id.of(2), true);
    assertEquals(1, application.informNodeJoinedCluster);
  }

  @Test
  public void testInformNodeLeftCluster() throws Exception {
    broadcaster.informNodeLeftCluster(Id.of(2), true);
    assertEquals(1, application.informNodeLeftCluster);
  }

  @Test
  public void testInformQuorumAchieved() throws Exception {
    broadcaster.informQuorumAchieved();
    assertEquals(1, application.informQuorumAchieved);
  }

  @Test
  public void testInformQuorumLost() throws Exception {
    broadcaster.informQuorumLost();
    assertEquals(1, application.informQuorumLost);
  }
  
  @Test
  public void testInformAttributeSetCreated() throws Exception {
    broadcaster.informAttributeSetCreated("test");
    assertEquals(1, application.informAttributeSetCreated);
  }
  
  @Test
  public void testInformAttributeAdded() throws Exception {
    broadcaster.informAttributeAdded("test", "test");
    assertEquals(1, application.informAttributeAdded);
  }
  
  @Test
  public void testInformAttributeRemoved() throws Exception {
    broadcaster.informAttributeRemoved("test", "test");
    assertEquals(1, application.informAttributeRemoved);
  }
  
  @Test
  public void testInformAttributeReplaced() throws Exception {
    broadcaster.informAttributeReplaced("test", "test");
    assertEquals(1, application.informAttributeReplaced);
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    broadcaster = new ClusterApplicationBroadcaster();
    broadcaster.registerClusterApplication(application);
  }
}

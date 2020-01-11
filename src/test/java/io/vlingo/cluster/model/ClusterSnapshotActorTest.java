// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.cluster.model.message.MessageConverters;
import io.vlingo.cluster.model.message.Pulse;
import io.vlingo.cluster.model.node.MergeResult;
import io.vlingo.cluster.model.node.RegistryInterest;
import io.vlingo.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

public class ClusterSnapshotActorTest extends AbstractClusterTest {
  private RawMessage opMessage;
  private ClusterSnapshotInitializer intializer;

  @Test
  public void testClusterSnapshot() throws Exception {
//    System.out.println("=========== testClusterSnapshot ===========");
//    System.out.println(" OP: " + intializer.localNode().operationalAddress());
//    System.out.println("APP: " + intializer.localNode().applicationAddress());

    final TestActor<ClusterSnapshot> snapshot =
            testWorld.actorFor(
                    ClusterSnapshot.class,
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)));

    snapshot.actor().quorumAchieved();
    assertEquals(1, application.informQuorumAchieved.get());

    snapshot.actor().quorumLost();
    assertEquals(1, application.informQuorumLost.get());
  }

  @Test
  public void testClusterSnapshotControl() throws Exception {
//    System.out.println("=========== testClusterSnapshotControl ===========");
//    System.out.println(" OP: " + intializer.localNode().operationalAddress());
//    System.out.println("APP: " + intializer.localNode().applicationAddress());
    final TestActor<ClusterSnapshotControl> control =
            testWorld.actorFor(
                    ClusterSnapshotControl.class,
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)));

    control.actor().shutDown();
    assertEquals(1, application.stop.get());
  }

  @Test
  public void testInboundStreamInterest() throws Exception {
//    System.out.println("=========== testInboundStreamInterest ===========");
//    System.out.println(" OP: " + intializer.localNode().operationalAddress());
//    System.out.println("APP: " + intializer.localNode().applicationAddress());
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    InboundStreamInterest.class,
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)));

    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, opMessage);
    assertEquals(0, application.handleApplicationMessage.get());

    final RawMessage appMessage = RawMessage.from(1, 0, "app-test");
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.APP, appMessage);
    assertEquals(1, application.handleApplicationMessage.get());
  }

  @Test
  public void testRegistryInterest() throws Exception {
//    System.out.println("=========== testRegistryInterest ===========");
//    System.out.println(" OP: " + intializer.localNode().operationalAddress());
//    System.out.println("APP: " + intializer.localNode().applicationAddress());
    final TestActor<RegistryInterest> registryInterest =
            testWorld.actorFor(
                    RegistryInterest.class,
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)));

    registryInterest.actor().informAllLiveNodes(config.allNodes(), true);
    assertEquals(1, application.allLiveNodes.get());

    registryInterest.actor().informConfirmedByLeader(config.nodeMatching(Id.of(1)), true);
    assertEquals(1, application.informNodeIsHealthy.get());

    registryInterest.actor().informCurrentLeader(config.nodeMatching(Id.of(3)), true);
    assertEquals(1, application.informLeaderElected.get());

    List<Node> nodes = new ArrayList<>();
    nodes.add(config.nodeMatching(Id.of(3)));
    List<MergeResult> mergeResult = new ArrayList<>();
    mergeResult.add(new MergeResult(config.nodeMatching(Id.of(2)), true));
    mergeResult.add(new MergeResult(config.nodeMatching(Id.of(1)), false));
    registryInterest.actor().informMergedAllDirectoryEntries(nodes, mergeResult, true);
    assertEquals(1, application.informNodeJoinedCluster.get());
    assertEquals(1, application.informNodeLeftCluster.get());

    registryInterest.actor().informLeaderDemoted(config.nodeMatching(Id.of(2)), true);
    assertEquals(1, application.informLeaderLost.get());

    registryInterest.actor().informNodeIsHealthy(config.nodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeIsHealthy.get());

    registryInterest.actor().informNodeJoinedCluster(config.nodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeJoinedCluster.get());

    registryInterest.actor().informNodeLeftCluster(config.nodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeLeftCluster.get());

    registryInterest.actor().informNodeTimedOut(config.nodeMatching(Id.of(2)), true);
    assertEquals(3, application.informNodeLeftCluster.get());
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    intializer = new ClusterSnapshotInitializer("node1", properties, testWorld.defaultLogger());
//    System.out.println("=========== setUp ===========");
//    System.out.println(" OP: " + properties.operationalPort("node1"));
//    System.out.println("APP: " + properties.applicationPort("node1"));

    final ByteBuffer messageBuffer = ByteBufferAllocator.allocate(4096);
    final Pulse pulse = new Pulse(Id.of(1));
    MessageConverters.messageToBytes(pulse, messageBuffer);
    opMessage = Converters.toRawMessage(Id.of(1).value(), messageBuffer);
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
  }
}

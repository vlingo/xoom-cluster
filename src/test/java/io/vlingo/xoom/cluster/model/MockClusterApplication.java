// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.xoom.cluster.model.application.ClusterApplication;
import io.vlingo.xoom.cluster.model.attribute.AttributesProtocol;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public class MockClusterApplication implements ClusterApplication {
  public AttributesProtocol attributesClient;

  public AtomicInteger allLiveNodes = new AtomicInteger(0);
  public AtomicInteger handleApplicationMessage = new AtomicInteger(0);

  public AtomicInteger informLeaderElected = new AtomicInteger(0);
  public AtomicInteger informLeaderLost = new AtomicInteger(0);
  public AtomicInteger informLocalNodeShutDown = new AtomicInteger(0);
  public AtomicInteger informLocalNodeStarted = new AtomicInteger(0);
  public AtomicInteger informNodeIsHealthy = new AtomicInteger(0);
  public AtomicInteger informClusterIsHealthy = new AtomicInteger(0);
  public AtomicInteger informNodeJoinedCluster = new AtomicInteger(0);
  public AtomicInteger informNodeLeftCluster = new AtomicInteger(0);
  public AtomicInteger informQuorumAchieved = new AtomicInteger(0);
  public AtomicInteger informQuorumLost = new AtomicInteger(0);
  public AtomicInteger informResponder = new AtomicInteger(0);

  public AtomicInteger informAttributesClient = new AtomicInteger(0);
  public AtomicInteger informAttributeSetCreated = new AtomicInteger(0);
  public AtomicInteger informAttributeAdded = new AtomicInteger(0);
  public AtomicInteger informAttributeRemoved = new AtomicInteger(0);
  public AtomicInteger informAttributeReplaced = new AtomicInteger(0);
  public AtomicInteger informAttributeSetRemoved = new AtomicInteger(0);

  public AtomicInteger stop = new AtomicInteger(0);

  @Override
  public void start() {

  }

  @Override
  public void conclude() {
    stop();
  }

  @Override
  public boolean isStopped() {
    return false;
  }

  @Override
  public void stop() {
    stop.incrementAndGet();
  }

  @Override
  public void handleApplicationMessage(RawMessage message) {
    handleApplicationMessage.incrementAndGet();
  }

  @Override
  public void informAllLiveNodes(Collection<Node> liveNodes, boolean isHealthyCluster) {
    allLiveNodes.incrementAndGet();
  }

  @Override
  public void informClusterIsHealthy(boolean isHealthyCluster) {
    informClusterIsHealthy.incrementAndGet();
  }

  @Override
  public void informNodeIsHealthy(Id nodeId, boolean isHealthyCluster) {
    informNodeIsHealthy.incrementAndGet();
  }

  @Override
  public void informNodeJoinedCluster(Id nodeId, boolean isHealthyCluster) {
    informNodeJoinedCluster.incrementAndGet();
  }

  @Override
  public void informNodeLeftCluster(Id nodeId, boolean isHealthyCluster) {
    informNodeLeftCluster.incrementAndGet();
  }

  @Override
  public void informResponder(final ApplicationOutboundStream responder) {
    informResponder.incrementAndGet();
  }

  @Override
  public void informAttributesClient(AttributesProtocol client) {
    attributesClient = client;
    informAttributesClient.incrementAndGet();
  }

  @Override
  public void informAttributeSetCreated(String attributeSetName) {
    informAttributeSetCreated.incrementAndGet();
  }

  @Override
  public void informAttributeAdded(String attributeSetName, String attributeName) {
    informAttributeAdded.incrementAndGet();
  }

  @Override
  public void informAttributeRemoved(String attributeSetName, String attributeName) {
    informAttributeRemoved.incrementAndGet();
  }

  @Override
  public void informAttributeSetRemoved(String attributeSetName) {
    informAttributeSetRemoved.incrementAndGet();
  }

  @Override
  public void informAttributeReplaced(String attributeSetName, String attributeName) {
    informAttributeReplaced.incrementAndGet();
  }
}

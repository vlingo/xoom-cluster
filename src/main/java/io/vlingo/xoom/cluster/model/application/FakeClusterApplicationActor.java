// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.application;

import java.util.Collection;

import io.vlingo.xoom.cluster.model.attribute.Attribute;
import io.vlingo.xoom.cluster.model.attribute.AttributesProtocol;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

public class FakeClusterApplicationActor extends ClusterApplicationAdapter {
  private AttributesProtocol client;
  private final Node localNode;

  public FakeClusterApplicationActor(final Node localNode) {
    this.localNode = localNode;
  }

  @Override
  public void start() {
    logger().debug("APP: ClusterApplication started on node: " + localNode);
  }

  @Override
  public void handleApplicationMessage(final RawMessage message) {
     logger().debug("APP: Received application message: " + message.asTextMessage());
  }

  @Override
  public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
    for (final Node id : liveNodes) {
       logger().debug("APP: Live node confirmed: " + id);
    }
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster) {
    logger().debug("APP: Node reported healthy: " + nodeId);
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
    logger().debug("APP: " + nodeId + " joined cluster");
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
    logger().debug("APP: " + nodeId + " left cluster");
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informClusterIsHealthy(boolean isHealthyCluster) {
    logger().debug("APP: Cluster is healthy");
    printHealthy(isHealthyCluster);
  }

  @Override
  public void informAttributesClient(final AttributesProtocol client) {
    logger().debug("APP: Attributes Client received.");
    this.client = client;
    if (localNode.id().value() == 1) {
      client.add("fake.set", "fake.attribute.name1", "value1");
      client.add("fake.set", "fake.attribute.name2", "value2");
    }
  }

  @Override
  public void informAttributeSetCreated(final String attributeSetName) {
     logger().debug("APP: Attributes Set Created: " + attributeSetName);
  }

  @Override
  public void informAttributeAdded(final String attributeSetName, final String attributeName) {
    final Attribute<String> attr = client.attribute(attributeSetName, attributeName);
    logger().debug("APP: Attribute Set " + attributeSetName + " Attribute Added: " + attributeName + " Value: " + attr.value);
    if (localNode.id().value() == 1) {
      client.replace("fake.set", "fake.attribute.name1", "value-replaced-2");
      client.replace("fake.set", "fake.attribute.name2", "value-replaced-20");
    }
  }

  @Override
  public void informAttributeRemoved(final String attributeSetName, final String attributeName) {
    final Attribute<String> attr = client.attribute(attributeSetName, attributeName);
    logger().debug("APP: Attribute Set " + attributeSetName + " Attribute Removed: " + attributeName + " Attribute: " + attr);
  }

  @Override
  public void informAttributeSetRemoved(final String attributeSetName) {
    logger().debug("APP: Attributes Set Removed: " + attributeSetName);
  }

  @Override
  public void informAttributeReplaced(final String attributeSetName, final String attributeName) {
    final Attribute<String> attr = client.attribute(attributeSetName, attributeName);
    logger().debug("APP: Attribute Set " + attributeSetName + " Attribute Replaced: " + attributeName + " Value: " + attr.value);
    if (localNode.id().value() == 1) {
      client.remove("fake.set", "fake.attribute.name1");
    }
  }

  private void printHealthy(final boolean isHealthyCluster) {
    if (isHealthyCluster) {
       logger().debug("APP: Cluster is healthy");
    } else {
       logger().debug("APP: Cluster is NOT healthy");
    }
  }
}

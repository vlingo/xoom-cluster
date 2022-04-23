package io.vlingo.xoom.cluster.model.application;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.cluster.model.attribute.AttributesProtocol;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.Collection;

public class ClusterApplicationAdapter extends Actor implements ClusterApplication {
  @Override
  public void start() {
  }

  @Override
  public void stop() {
  }

  @Override
  public void informAllLiveNodes(Collection<Node> liveNodes, boolean isHealthyCluster) {
  }

  @Override
  public void informNodeJoinedCluster(Id nodeId, boolean isHealthyCluster) {
  }

  @Override
  public void informNodeLeftCluster(Id nodeId, boolean isHealthyCluster) {
  }

  @Override
  public void informNodeIsHealthy(Id nodeId, boolean isHealthyCluster) {
  }

  @Override
  public void informClusterIsHealthy(boolean isHealthyCluster) {
  }

  @Override
  public void informAttributesClient(AttributesProtocol client) {
  }

  @Override
  public void informAttributeSetCreated(String attributeSetName) {
  }

  @Override
  public void informAttributeAdded(String attributeSetName, String attributeName) {
  }

  @Override
  public void informAttributeRemoved(String attributeSetName, String attributeName) {
  }

  @Override
  public void informAttributeSetRemoved(String attributeSetName) {
  }

  @Override
  public void informAttributeReplaced(String attributeSetName, String attributeName) {
  }

  @Override
  public void informResponder(ApplicationOutboundStream responder) {
  }

  @Override
  public void handleApplicationMessage(RawMessage message) {
  }
}

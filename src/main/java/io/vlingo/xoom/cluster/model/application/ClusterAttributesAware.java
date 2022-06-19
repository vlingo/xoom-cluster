package io.vlingo.xoom.cluster.model.application;

import io.vlingo.xoom.cluster.model.attribute.AttributesProtocol;

public interface ClusterAttributesAware {
  void informAttributesClient(final AttributesProtocol client);
  void informAttributeSetCreated(final String attributeSetName);
  void informAttributeAdded(final String attributeSetName, final String attributeName);
  void informAttributeRemoved(final String attributeSetName, final String attributeName);
  void informAttributeSetRemoved(final String attributeSetName);
  void informAttributeReplaced(final String attributeSetName, final String attributeName);
}

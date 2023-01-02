// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.attribute.message;

import io.vlingo.xoom.cluster.model.attribute.Attribute;
import io.vlingo.xoom.cluster.model.message.ApplicationSays;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;

import java.util.HashMap;
import java.util.Map;

public final class ReceivedAttributeMessage {
  private static final String SourceNodeIdKey = "sourceNodeIdKey";
  private static final String SourceNodeNameKey = "sourceNodeNameKey";
  private static final String SourceNodeOpPortKey = "sourceNodeOpPortKey";
  private static final String SourceNodeAppPortKey = "sourceNodeAppPortKey";
  
  private static final String ClassOfMessageKey = "classOfMessage";
  private static final String CorrelatingMessageIdKey = "correlatingMessageId";
  private static final String MessageTypeKey = "type";
  private static final String TrackingIdKey = "trackingId";
  
  private static final String AttributeSetNameKey = "attributeSetName";
  
  private static final String AttributeNameKey = "attributeName";
  private static final String AttributeTypeKey = "attributeType";
  private static final String AttributeValueKey = "attributeValue";

  private final Map<String,String> payloadMap;
  
  public ReceivedAttributeMessage(final RawMessage message) {
    this.payloadMap = parsePayload(message);
  }

  public Id sourceNodeId() {
    return Id.of(Integer.parseInt(payloadMap.get(SourceNodeIdKey)));
  }

  public Name sourceNodeName() {
    return Name.of(payloadMap.get(SourceNodeNameKey));
  }

  public Id sourceNodeOpPort() {
    return Id.of(Integer.parseInt(payloadMap.get(SourceNodeOpPortKey)));
  }

  public Id sourceNodeAppPort() {
    return Id.of(Integer.parseInt(payloadMap.get(SourceNodeAppPortKey)));
  }

  public String classOfMessage() {
    return payloadMap.get(ClassOfMessageKey);
  }

  public String correlatingMessageId() {
    return payloadMap.get(CorrelatingMessageIdKey);
  }

  public String trackingId() {
    return payloadMap.get(TrackingIdKey);
  }
  
  public ApplicationMessageType type() {
    return ApplicationMessageType.valueOf(payloadMap.get(MessageTypeKey));
  }
  
  public Attribute<?> attribute() {
    final Attribute.Type type = Attribute.Type.valueOf(Attribute.Type.class, attributeType());
    return Attribute.from(attributeName(), type, attributeValue());
  }
  
  public String attributeSetName() {
    return payloadMap.get(AttributeSetNameKey);
  }
  
  public String attributeName() {
    return payloadMap.get(AttributeNameKey);
  }
  
  public String attributeType() {
    return payloadMap.get(AttributeTypeKey);
  }
  
  public String attributeValue() {
    return payloadMap.get(AttributeValueKey);
  }

  @Override
  public String toString() {
    return "ReceivedAttributeMessage[payloadMap=" + payloadMap + "]";
  }

  private Map<String,String> parsePayload(final RawMessage message) {
    final Map<String,String> map = new HashMap<>();
    
    final ApplicationSays says = ApplicationSays.from(message.asTextMessage());
    
    map.put(SourceNodeIdKey, says.id().valueString());
    map.put(SourceNodeNameKey, says.name().value());
    
    final String[] parsed = says.payload().split("\n");
    
    map.put(ClassOfMessageKey, parsed[0]);

    switch (parsed[0]) {
      case "ConfirmCreateAttributeSet":
      case "ConfirmRemoveAttributeSet":
        map.put(CorrelatingMessageIdKey, parsed[1]);
        map.put(TrackingIdKey, parsed[2]);
        map.put(MessageTypeKey, parsed[3]);
        map.put(AttributeSetNameKey, parsed[4]);
        break;
      case "ConfirmAttribute":
        map.put(CorrelatingMessageIdKey, parsed[1]);
        map.put(TrackingIdKey, parsed[2]);
        map.put(MessageTypeKey, parsed[3]);
        map.put(AttributeSetNameKey, parsed[4]);
        map.put(AttributeNameKey, parsed[5]);
        break;
      case "CreateAttributeSet":
      case "RemoveAttributeSet":
        map.put(TrackingIdKey, parsed[1]);
        map.put(MessageTypeKey, parsed[2]);
        map.put(AttributeSetNameKey, parsed[3]);
        break;
      case "AddAttribute":
      case "RemoveAttribute":
      case "ReplaceAttribute":
        map.put(CorrelatingMessageIdKey, parsed[1]);
        map.put(TrackingIdKey, parsed[2]);
        map.put(MessageTypeKey, parsed[3]);
        map.put(AttributeSetNameKey, parsed[4]);
        map.put(AttributeNameKey, parsed[5]);
        map.put(AttributeTypeKey, parsed[6]);
        map.put(AttributeValueKey, parsed[7]);
        break;
    }
    
    return map;
  }
}

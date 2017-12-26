// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.vlingo.cluster.model.AbstractMessageTool;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.common.message.RawMessage;

public class MockInboundStreamInterest extends AbstractMessageTool implements InboundStreamInterest {
  private final ByteBuffer buffer;
  public int messageCount;
  public final List<String> messages = new ArrayList<>();

  public MockInboundStreamInterest() {
    this.buffer = ByteBuffer.allocate(1024);
  }
  
  @Override
  public void handleInboundStreamMessage(final AddressType addressType, final RawMessage message, final InboundResponder responder) {
    ++messageCount;
    final String textMessage = message.asTextMessage();
    messages.add(textMessage);
    
    try {
      final RawMessage responseMessage = buildRawMessageBuffer(buffer, "RESPOND-FOR: " + textMessage);
      
      responder.respondWith(responseMessage.asByteBuffer());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

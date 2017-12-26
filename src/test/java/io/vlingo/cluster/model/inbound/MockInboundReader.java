// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.vlingo.cluster.model.AbstractMessageTool;
import io.vlingo.common.message.RawMessage;

public class MockInboundReader extends AbstractMessageTool implements InboundReader {
  public static final String MessagePrefix = "Message-";
  
  public int inboundClientChannelWriteCount;
  public int probeChannelCount;

  private final ByteBuffer buffer;
  private final MockInboundClientChannel clientChannel;
  private InboundReaderConsumer consumer;
  
  public MockInboundReader() {
    this.buffer = ByteBuffer.allocate(1024);
    this.clientChannel = new MockInboundClientChannel();
  }
  
  @Override
  public void close() {

  }

  @Override
  public String inboundName() {
    return "mock";
  }

  @Override
  public void openFor(final InboundReaderConsumer consumer) throws IOException {
    this.consumer = consumer;
  }

  @Override
  public void probeChannel() {
    ++probeChannelCount;
    
    final RawMessage message = buildRawMessageBuffer(buffer, MessagePrefix + probeChannelCount);
    
    consumer.consume(message, clientChannel);
  }

  private class MockInboundClientChannel implements InboundClientChannel {
    @Override
    public void writeBackResponse(ByteBuffer buffer) {
      ++inboundClientChannelWriteCount;
    }
  }
}

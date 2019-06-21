// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Logger;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Configuration;

import java.nio.ByteBuffer;

public class AbstractMessageTool {
  protected Configuration config = new ClusterConfiguration(Logger.basicLogger());
  
  public RawMessage buildRawMessageBuffer(final ByteBuffer buffer, final String message) {
    buffer.clear();
    buffer.put(Converters.textToBytes(message));
    buffer.flip();
    final RawMessage rawMessage = RawMessage.from(1, 0, buffer.limit());
    rawMessage.put(buffer, false);
    
    return rawMessage;
  }
  
  public ByteBuffer bytesFrom(final RawMessage message, final ByteBuffer buffer) {
    buffer.clear();
    message.copyBytesTo(buffer);
    buffer.flip();
    return buffer;
  }
}

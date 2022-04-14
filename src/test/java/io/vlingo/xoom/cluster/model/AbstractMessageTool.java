// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import java.nio.ByteBuffer;

import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.message.RawMessage;

public abstract class AbstractMessageTool {
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

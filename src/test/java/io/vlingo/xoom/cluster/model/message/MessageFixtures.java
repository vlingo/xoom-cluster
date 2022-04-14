// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.xoom.wire.message.ByteBufferAllocator;
import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.message.RawMessage;
import io.vlingo.xoom.wire.message.RawMessageHeader;

public class MessageFixtures {
  public static AtomicInteger nextPortNumber = new AtomicInteger(27270);

  private static final short defaultNodeId = 1;
  private static final String defaultNodeName = "node1";
  private static final String[] opAddresses = { "", "localhost:{0}", "localhost:{0}", "localhost:{0}" };
  private static final String[] appAddresses = { "", "localhost:{0}", "localhost:{0}", "localhost:{0}" };

  public static short defaultNodeId() {
    return defaultNodeId;
  }

  public static String defaultNodeName() {
    return defaultNodeName;
  }

  public static String appAddress(final int index) {
    return MessageFormat.format(appAddresses[index], Integer.toString(nextPortNumber.incrementAndGet()));
  }

  public static String opAddress(final int index) {
    return MessageFormat.format(opAddresses[index], Integer.toString(nextPortNumber.incrementAndGet()));
  }

  public static String directoryAsText(final int id1, final int id2, final int id3) {
    StringBuilder builder =
        new StringBuilder(OperationalMessage.DIR)
        .append("\n")
        .append("id=").append(id1).append(" nm=node").append(id1).append("\n")
        .append("id=").append(id1).append(" nm=node").append(id1).append(" op=").append(opAddress(1)).append(" app=").append(appAddress(1)).append("\n")
        .append("id=").append(id2).append(" nm=node").append(id2).append(" op=").append(opAddress(2)).append(" app=").append(appAddress(2)).append("\n")
        .append("id=").append(id3).append(" nm=node").append(id3).append(" op=").append(opAddress(3)).append(" app=").append(appAddress(3));

    return builder.toString();
  }

  public static String joinAsText() {
    return OperationalMessage.JOIN + "\nid=1 nm=node1 op=" + opAddress(1) + " app=" + appAddress(1);
  }

  public static String leaderAsText() {
    return OperationalMessage.LEADER + "\n" + "id=1";
  }

  public static String leaveAsText() {
    return OperationalMessage.LEAVE + "\n" + "id=1";
  }

  public static ByteBuffer bytesFrom(final String text) {
    final RawMessage message = new RawMessage(Converters.textToBytes(text));
    final RawMessageHeader header = RawMessageHeader.from(1, 0, message.length());
    message.header(header);

    final ByteBuffer buffer = ByteBufferAllocator.allocate(4000);
    message.copyBytesTo(buffer);
    buffer.flip();

    return buffer;
  }
}

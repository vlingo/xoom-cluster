// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.nio.ByteBuffer;

import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.message.RawMessageHeader;

public class MessageFixtures {

  public static final short defaultNodeId = 1;
  public static final String defaultNodeName = "node1";
  public static final String[] opAddresses = { "", "localhost:37371", "localhost:37373", "localhost:37375" };
  public static final String[] appAddresses = { "", "localhost:37372", "localhost:37374", "localhost:37376" };
  
  public static String directoryAsText(final int id1, final int id2, final int id3) {
    StringBuilder builder =
        new StringBuilder(OperationalMessage.DIR)
        .append("\n")
        .append("id=").append(id1).append(" nm=node").append(id1).append("\n")
        .append("id=").append(id1).append(" nm=node").append(id1).append(" op=").append(opAddresses[1]).append(" app=").append(appAddresses[1]).append("\n")
        .append("id=").append(id2).append(" nm=node").append(id2).append(" op=").append(opAddresses[2]).append(" app=").append(appAddresses[2]).append("\n")
        .append("id=").append(id3).append(" nm=node").append(id3).append(" op=").append(opAddresses[3]).append(" app=").append(appAddresses[3]);

    return builder.toString();
  }
  
  public static String joinAsText() {
    return OperationalMessage.JOIN + "\nid=1 nm=node1 op=localhost:37371 app=localhost:37372";
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

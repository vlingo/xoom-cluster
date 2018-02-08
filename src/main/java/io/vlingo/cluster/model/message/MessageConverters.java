// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.nio.ByteBuffer;

import io.vlingo.wire.message.Converters;
import io.vlingo.wire.node.Node;

public class MessageConverters {
  public static void messageToBytes(final ApplicationSays app, final ByteBuffer buffer) {
    final StringBuilder builder = new StringBuilder(OperationalMessage.APP).append("\n");
    
    builder
      .append("id=").append(app.id().value())
      .append(" nm=").append(app.name().value())
      .append(" si=").append(app.saysId())
      .append("\n").append(app.payload());
    
    final byte[] bytes = Converters.textToBytes(builder.toString());

    buffer.put(bytes);
  }

  public static void messageToBytes(final Directory dir, final ByteBuffer buffer) {
    final StringBuilder builder = new StringBuilder(OperationalMessage.DIR).append("\n");

    builder.append("id=").append(dir.id().value()).append(" nm=").append(dir.name().value()).append("\n");

    String lf = "";

    for (Node node : dir.nodes()) {
      builder
          .append(lf)
          .append("id=").append(node.id().value())
          .append(" nm=").append(node.name().value())
          .append(" op=").append(node.operationalAddress().hostName())
                            .append(":").append(node.operationalAddress().port())
          .append(" app=").append(node.applicationAddress().hostName())
                            .append(":").append(node.applicationAddress().port());

      lf = "\n";
    }

    final byte[] bytes = Converters.textToBytes(builder.toString());

    buffer.put(bytes);
  }

  public static void messageToBytes(final Elect elect, final ByteBuffer buffer) {
    basicMessageToBytes(elect, OperationalMessage.ELECT, buffer);
  }

  public static void messageToBytes(final Join join, final ByteBuffer buffer) {
    final StringBuilder builder =
        new StringBuilder(OperationalMessage.JOIN)
            .append("\n")
            .append("id=")
            .append(join.node().id().value())
            .append(" nm=")
            .append(join.node().name().value())
            .append(" op=")
            .append(join.node().operationalAddress().hostName())
            .append(":")
            .append(join.node().operationalAddress().port())
            .append(" app=")
            .append(join.node().applicationAddress().hostName())
            .append(":")
            .append(join.node().applicationAddress().port());

    final byte[] bytes = Converters.textToBytes(builder.toString());

    buffer.put(bytes);
  }

  public static void messageToBytes(final Leader leader, final ByteBuffer buffer) {
    basicMessageToBytes(leader, OperationalMessage.LEADER, buffer);
  }

  public static void messageToBytes(final Leave leave, final ByteBuffer buffer) {
    basicMessageToBytes(leave, OperationalMessage.LEAVE, buffer);
  }

  public static void messageToBytes(final Ping ping, final ByteBuffer buffer) {
    basicMessageToBytes(ping, OperationalMessage.PING, buffer);
  }

  public static void messageToBytes(final Pulse pulse, final ByteBuffer buffer) {
    basicMessageToBytes(pulse, OperationalMessage.PULSE, buffer);
  }

  public static void messageToBytes(final Split split, final ByteBuffer buffer) {
    basicMessageToBytes(split, OperationalMessage.SPLIT, buffer);
  }

  public static void messageToBytes(final Vote vote, final ByteBuffer buffer) {
    basicMessageToBytes(vote, OperationalMessage.VOTE, buffer);
  }

  private static void basicMessageToBytes(final OperationalMessage message, final String type, final ByteBuffer buffer) {
    final StringBuilder builder =
        new StringBuilder(type)
            .append("\n")
            .append("id=")
            .append(message.id().value());

    final byte[] bytes = Converters.textToBytes(builder.toString());

    buffer.clear();
    buffer.put(bytes);
  }
}

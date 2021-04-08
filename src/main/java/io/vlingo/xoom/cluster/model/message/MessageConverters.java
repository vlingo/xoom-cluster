// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import java.nio.ByteBuffer;

import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.node.Node;

public class MessageConverters {
  public static void messageToBytes(final ApplicationSays app, final ByteBuffer buffer) {
    final String message = OperationalMessage.APP + "\n" +
        "id=" + app.id().value() +
        " nm=" + app.name().value() +
        " si=" + app.saysId() +
        "\n" + app.payload();

    final byte[] bytes = Converters.textToBytes(message);
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
    final String message = OperationalMessage.JOIN +
        "\n" +
        "id=" +
        join.node().id().value() +
        " nm=" +
        join.node().name().value() +
        " op=" +
        join.node().operationalAddress().hostName() +
        ":" +
        join.node().operationalAddress().port() +
        " app=" +
        join.node().applicationAddress().hostName() +
        ":" +
        join.node().applicationAddress().port();
    final byte[] bytes = Converters.textToBytes(message);

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

    final String _message = type +
        "\n" +
        "id=" +
        message.id().value();
    final byte[] bytes = Converters.textToBytes(_message);

    buffer.clear();
    buffer.put(bytes);
  }
}

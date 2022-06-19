// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
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

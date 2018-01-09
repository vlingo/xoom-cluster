// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.util.HashSet;
import java.util.Set;

import io.vlingo.cluster.model.node.Address;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;
import io.vlingo.cluster.model.node.Node;

class OperationalMessagePartsBuilder {
  protected static final Set<Node> nodesFrom(String content) {
    final Set<Node> nodeEntries = new HashSet<Node>();

    final String[] parts = content.split("\n");

    if (parts.length < 3) {
      return nodeEntries;
    }

    for (int index = 2; index < parts.length; ++index) {
      nodeEntries.add(nodeFromRecord(parts[index]));
    }

    return nodeEntries;
  }

  protected static final Node nodeFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 2) {
      return Node.NO_NODE;
    }

    return nodeFromRecord(parts[1]);
  }

  protected static final Id idFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 2) {
      return Id.NO_ID;
    }

    return idFromRecord(parts[1]);
  }

  protected static final Name nameFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 2) {
      return Name.NO_NODE_NAME;
    }

    return nameFromRecord(parts[1]);
  }

  public static String payloadFrom(final String content) {
    final int index1 = content.indexOf('\n');
    if (index1 == -1) return "";
    final int index2 = content.indexOf('\n', index1+1);
    if (index2 == -1) return "";
    final String payload = content.substring(index2+1);
    
    return payload;
  }

  public static String saysIdFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 3) {
      return "";
    }

    final String saysId = parseField(parts[2], "si=");
    
    return saysId;
  }

  private static final Address addressFromRecord(final String record, final AddressType type) {
    final String text = parseField(record, type == AddressType.OP ? "op=" : "msg=");

    if (text == null) {
      return Address.NO_NODE_ADDRESS;
    }

    return Address.from(text, type);
  }

  private static final Node nodeFromRecord(final String record) {
    final Id id = idFromRecord(record);
    final Name name = nameFromRecord(record);
    final Address opAddress = addressFromRecord(record, AddressType.OP);
    final Address appAddress = addressFromRecord(record, AddressType.APP);

    return new Node(id, name, opAddress, appAddress);
  }

  private static final Id idFromRecord(final String record) {
    final String text = parseField(record, "id=");

    if (text == null) {
      return Id.NO_ID;
    }

    return Id.of(Short.parseShort(text));
  }

  private static final Name nameFromRecord(final String record) {
    final String text = parseField(record, "nm=");

    if (text == null) {
      return Name.NO_NODE_NAME;
    }

    return new Name(text);
  }

  private static final String parseField(final String record, final String fieldName) {
    final String skinnyRecord = record.trim();

    final int idIndex = skinnyRecord.indexOf(fieldName);

    if (idIndex == -1) {
      return null;
    }

    final int valueIndex = idIndex + fieldName.length();
    final int space = skinnyRecord.indexOf(' ', valueIndex);

    if (valueIndex >= space) {
      return skinnyRecord.substring(valueIndex);
    }

    return skinnyRecord.substring(valueIndex, space);
  }
}

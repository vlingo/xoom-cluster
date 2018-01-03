// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

public final class Address {
  public static final String NO_HOST = "?";
  public static final int NO_PORT = -1;
  public static final Address NO_NODE_ADDRESS = new Address(NO_HOST, NO_PORT, AddressType.NONE);

  private final String host;
  private final int port;
  private final AddressType type;

  public static Address from(final String fullAddress, final AddressType type) {
    String[] parts = fullAddress.split(":");

    if (parts.length != 2) {
      throw new IllegalArgumentException("The address is not valid: " + fullAddress);
    }

    return new Address(parts[0], Integer.parseInt(parts[1]), type);
  }

  public static Address from(final String host, final int port, final AddressType type) {
    return new Address(host, port, type);
  }

  public Address(final String host, final int port, final AddressType type) {
    this.host = host;
    this.port = port;
    this.type = type;
  }

  public final String host() {
    return host;
  }

  public boolean hasNoAddress() {
    return host().equals(NO_HOST) || port() == NO_PORT;
  }

  public boolean isValid() {
    return !hasNoAddress();
  }

  public int port() {
    return port;
  }

  public AddressType type() {
    return type;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != Address.class) {
      return false;
    }

    return this.host.equals(((Address) other).host);
  }

  @Override
  public int hashCode() {
    return 31 * host.hashCode();
  }

  @Override
  public String toString() {
    return "NodeAddress[" + host + "," + port + "]";
  }
}

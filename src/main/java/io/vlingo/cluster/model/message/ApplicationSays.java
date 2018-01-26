// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.util.UUID;

import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;

public final class ApplicationSays extends OperationalMessage {
  public final Name name;
  public final String payload;
  public final String saysId;

  public static final ApplicationSays from(final String content) {
    final Id id = OperationalMessagePartsBuilder.idFrom(content);
    final Name name = OperationalMessagePartsBuilder.nameFrom(content);
    final String saysId = OperationalMessagePartsBuilder.saysIdFrom(content);
    final String payload = OperationalMessagePartsBuilder.payloadFrom(content);

    return new ApplicationSays(id, name, saysId, payload);
  }

  public static ApplicationSays from(final Id id, final Name name, final String payload) {
    return new ApplicationSays(id, name, payload);
  }

  private ApplicationSays(final Id id, final Name name, final String payload) {
    super(id);
    
    this.name = name;
    this.payload = payload;
    this.saysId = UUID.randomUUID().toString();
  }

  private ApplicationSays(final Id id, final Name name, final String saysId, final String payload) {
    super(id);
    
    this.name = name;
    this.saysId = saysId;
    this.payload = payload;
  }

  @Override
  public boolean isApp() {
    return true;
  }

  public final Name name() {
    return name;
  }

  public final String payload() {
    return payload;
  }

  public final String saysId() {
    return saysId;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != ApplicationSays.class) {
      return false;
    }
    
    final ApplicationSays otherAppSaid = (ApplicationSays) other;

    return this.name.equals(otherAppSaid.name) && this.payload.equals(otherAppSaid.payload);
  }

  @Override
  public int hashCode() {
    return 31 * name.hashCode() + payload.hashCode();
  }

  @Override
  public String toString() {
    return "ApplicationSays[" + id() + "," + name + "," + payload + "]";
  }
}

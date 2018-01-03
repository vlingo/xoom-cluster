// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;

public final class ApplicationSaid extends OperationalMessage {
  private final Name name;
  private final String payload;

  public static final ApplicationSaid from(final String content) {
    final Id id = OperationalMessagePartsBuilder.idFrom(content);
    final Name name = OperationalMessagePartsBuilder.nameFrom(content);
    final String payload = OperationalMessagePartsBuilder.payloadFrom(content);

    return new ApplicationSaid(id, name, payload);
  }

  public ApplicationSaid(final Id id, final Name name, final String payload) {
    super(id);
    
    this.name = name;
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

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != ApplicationSaid.class) {
      return false;
    }
    
    final ApplicationSaid otherAppSaid = (ApplicationSaid) other;

    return this.name.equals(otherAppSaid.name) && this.payload.equals(otherAppSaid.payload);
  }

  @Override
  public int hashCode() {
    return 31 * name.hashCode() + payload.hashCode();
  }

  @Override
  public String toString() {
    return "ApplicationSaid[" + id() + "," + name + "," + payload + "]";
  }
}

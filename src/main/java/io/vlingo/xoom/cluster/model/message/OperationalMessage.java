// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import io.vlingo.xoom.wire.message.Message;
import io.vlingo.xoom.wire.node.Id;

public abstract class OperationalMessage implements Message {

  /**
   * APP&lt;lf&gt;id=x nm=name si=y&lt;lf&gt;...
   */
  public static final String APP = "APP";

  /**
   * Answer a new concrete OperationalMessage from the content.
   * @param content the String containing the message text
   * @return OperationalMessage
   */
  public static OperationalMessage messageFrom(final String content) {
    if (content.startsWith(APP)) {
      return ApplicationSays.from(content);
    }
    return null;
  }

  protected final Id id;

  public boolean isApp() {
    return false;
  }

  public final Id id() {
    return id;
  }

  protected OperationalMessage(final Id id) {
    this.id = id;
  }
}

// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
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
   * CHECKHEALTH&lt;lf&gt;id=x ***internal only*** to check health
   */
  public static final String CHECKHEALTH = "CHECKHEALTH";

  /**
   * DIR&lt;lf&gt;id=x nm=name&lt;lf&gt;addr=...&lt;lf&gt;...
   */
  public static final String DIR = "DIR";

  /**
   * ELECT&lt;lf&gt;id=x an election is required
   */
  public static final String ELECT = "ELECT";

  /**
   * JOIN&lt;lf&gt;addr=... node joining cluster
   */
  public static final String JOIN = "JOIN";

  /**
   * LEADER&lt;lf&gt;id=x declare self as leader (highest existing node)
   */
  public static final String LEADER = "LEADER";

  /**
   * LEAVE&lt;lf&gt;id=x announce that node is leaving the cluster
   */
  public static final String LEAVE = "LEAVE";

  /**
   * PING&lt;lf&gt;id=x leader sends ping to follower to determine if it is alive
   */
  public static final String PING = "PING";

  /**
   * PULSE&lt;lf&gt;id=x follower sends pulse to leader
   */
  public static final String PULSE = "PULSE";

  /**
   * SPLIT&lt;lf&gt;id=x inform receiving node that it is split from known leader
   */
  public static final String SPLIT = "SPLIT";

  /**
   * VOTE&lt;lf&gt;id=x a vote is made by all higher nodes
   */
  public static final String VOTE = "VOTE";

  /**
   * Answer a new concrete OperationalMessage from the content.
   * @param content the String containing the message text
   * @return OperationalMessage
   */
  public static OperationalMessage messageFrom(final String content) {
    if (content.startsWith(APP)) {
      return ApplicationSays.from(content);
    } else if (content.startsWith(DIR)) {
      return Directory.from(content);
    } else if (content.startsWith(ELECT)) {
      return Elect.from(content);
    } else if (content.startsWith(JOIN)) {
      return Join.from(content);
    } else if (content.startsWith(LEADER)) {
      return Leader.from(content);
    } else if (content.startsWith(LEAVE)) {
      return Leave.from(content);
    } else if (content.startsWith(PING)) {
      return Ping.from(content);
    } else if (content.startsWith(PULSE)) {
      return Pulse.from(content);
    } else if (content.startsWith(SPLIT)) {
      return Split.from(content);
    } else if (content.startsWith(VOTE)) {
      return Vote.from(content);
    }
    return null;
  }

  protected final Id id;

  public boolean isApp() {
    return false;
  }

  public boolean isDirectory() {
    return false;
  }

  public boolean isElect() {
    return false;
  }

  public boolean isCheckHealth() {
    return false;
  }

  public boolean isJoin() {
    return false;
  }

  public boolean isLeader() {
    return false;
  }

  public boolean isLeave() {
    return false;
  }

  public boolean isPing() {
    return false;
  }

  public boolean isPulse() {
    return false;
  }

  public boolean isSplit() {
    return false;
  }

  public boolean isVote() {
    return false;
  }

  public final Id id() {
    return id;
  }

  protected OperationalMessage(final Id id) {
    this.id = id;
  }
}

// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

final class TimeoutTracker {
  private boolean cleared = false;
  private long startTime = -1L;
  private final long timeout;

  TimeoutTracker(final long timeout) {
    this.timeout = timeout;
  }

  void clear() {
    cleared = true;
    reset();
  }

  boolean hasTimedOut() {
    if (!cleared && startTime > 0) {
      final long currentTime = System.currentTimeMillis();

      return currentTime >= startTime + timeout;
    }
    return false;
  }

  void reset() {
    startTime = -1L;
  }

  boolean hasNotStarted() {
    return startTime <= 0;
  }

  void start() {
    start(false);
  }

  void start(final boolean force) {
    if (force && startTime == -1L || !cleared && startTime == -1L) {
      cleared = false;
      startTime = System.currentTimeMillis();
    }
  }
}

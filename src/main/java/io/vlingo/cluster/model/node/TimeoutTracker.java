// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

final class TimeoutTracker {
  private boolean cleared = false;
  private long startTime = -1L;
  private final long timeout;

  protected TimeoutTracker(final long timeout) {
    this.timeout = timeout;
  }

  protected void clear() {
    cleared = true;
    reset();
  }

  protected boolean hasTimedOut() {
    if (!cleared && startTime > 0) {
      final long currentTime = System.currentTimeMillis();

      return currentTime >= startTime + timeout;
    }
    return false;
  }

  protected void reset() {
    startTime = -1L;
  }

  protected boolean hasStarted() {
    return startTime > 0;
  }

  protected void start() {
    start(false);
  }

  protected void start(final boolean force) {
    if (force && startTime == -1L || !cleared && startTime == -1L) {
      cleared = false;
      startTime = System.currentTimeMillis();
    }
  }
}

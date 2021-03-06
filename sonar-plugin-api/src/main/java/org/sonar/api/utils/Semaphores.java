/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.api.utils;

import org.sonar.api.BatchComponent;
import org.sonar.api.ServerComponent;

import java.util.Date;

/**
 * A semaphore shared among all the processes that can connect to the central database.
 * It's ignored when enabling the dry run mode.
 *
 * @since 3.4
 */
public interface Semaphores extends BatchComponent, ServerComponent {

  /**
   * Try to acquire a semaphore for a given duration.
   * The semaphore is acquired if it's unlocked or if the max locking duration is reached.
   *
   * @param name                 the key of the semaphore
   * @param maxDurationInSeconds the max duration in seconds the semaphore will be acquired. The value zero forces the semaphore to be acquired, whatever its status.
   * @return the semaphore, whatever its status (locked or unlocked). Can't be null.
   */
  Semaphore acquire(String name, int maxDurationInSeconds);

  /**
   * Try to acquire a semaphore.
   * The lock will be acquired only if there's no existing lock.
   *
   * @param name the key of the semaphore
   * @return a lock containing information if the lock could be acquired or not, the duration since locked, etc.
   */
  Semaphore acquire(String name);

  /**
   * Release the lock on a semaphore by its name. Does nothing if the lock is already released.
   *
   * @param name the key of the semaphore
   */
  void release(String name);

  class Semaphore {

    private String name;
    private boolean locked;
    private Date locketAt;
    private Date createdAt;
    private Date updatedAt;
    private Long durationSinceLocked;

    public String getName() {
      return name;
    }

    public Semaphore setName(String name) {
      this.name = name;
      return this;
    }

    public boolean isLocked() {
      return locked;
    }

    public Semaphore setLocked(boolean locked) {
      this.locked = locked;
      return this;
    }

    public Date getLocketAt() {
      return locketAt;
    }

    public Semaphore setLocketAt(Date locketAt) {
      this.locketAt = locketAt;
      return this;
    }

    public Date getCreatedAt() {
      return createdAt;
    }

    public Semaphore setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Date getUpdatedAt() {
      return updatedAt;
    }

    public Semaphore setUpdatedAt(Date updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Long getDurationSinceLocked() {
      return durationSinceLocked;
    }

    public Semaphore setDurationSinceLocked(Long durationSinceLocked) {
      this.durationSinceLocked = durationSinceLocked;
      return this;
    }
  }

}

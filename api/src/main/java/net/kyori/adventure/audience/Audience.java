/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.audience;

import java.util.Arrays;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A receiver of text-based media.
 */
public interface Audience {
  /**
   * Gets an audience that does nothing.
   *
   * @return an audience
   */
  static @NonNull Audience empty() {
    return EmptyAudience.INSTANCE;
  }

  /**
   * Creates an audience that delegates to an array of audiences.
   *
   * @param audiences the delegate audiences
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Audience@NonNull... audiences) {
    final int length = audiences.length;
    if(length == 0) {
      return empty();
    } else if(length == 1) {
      return audiences[0];
    }
    return of(Arrays.asList(audiences));
  }

  /**
   * Creates an audience that delegates to a collection of audiences.
   *
   * @param audiences the delegate audiences
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Iterable<? extends Audience> audiences) {
    return (MultiAudience) () -> audiences;
  }

  /**
   * Creates an audience that weakly delegates to another audience.
   *
   * @param audience the delegate audience
   * @return an audience
   */
  static @NonNull Audience weakOf(final @Nullable Audience audience) {
    return audience instanceof WeakAudience || audience instanceof EmptyAudience ? audience : new WeakAudience(audience);
  }

  // ------------------
  // ---- Messages ----
  // ------------------

  /**
   * Get if there is any way this audience can receive a message.
   *
   * <p>Sending a message when this method returns {@code false} will be a no-op.
   * This method returning true does not guarantee that a message send will succeed.</p>
   *
   * @return false if messages cannot be sent to this source
   */
  boolean canSendMessage();

  /**
   * Sends a message.
   *
   * @param message the message
   */
  void sendMessage(final @NonNull Component message);

  // --------------------
  // ---- Action Bar ----
  // --------------------

  /**
   * Get if there is any way this audience can receive an action bar.
   *
   * <p>Sending to the action bar when this method returns {@code false} will be a no-op.
   * This method returning true does not guarantee that an action bar send will succeed.</p>
   *
   * @return false if this source cannot receive action bars
   */
  boolean canSendActionBar();

  /**
   * Sends a message on the action bar.
   *
   * @param message the message
   */
  void sendActionBar(final @NonNull Component message);

  // ----------------
  // ---- Titles ----
  // ----------------

  /**
   * Get if there is any way this audience can receive a title.
   *
   * <p>Sending a title when this method returns {@code false} will be a no-op.
   * This method returning true does not guarantee that a title send will succeed.</p>
   *
   * @return false if titles cannot be sent to this source
   */
  boolean canShowTitle();

  /**
   * Shows a title.
   *
   * @param title the title
   */
  void showTitle(final @NonNull Title title);

  /**
   * Clears the currently displayed title.
   */
  void clearTitle();

  /**
   * Resets the title, subtitle, fade-in time, stay time, and fade-out time back to "unset".
   */
  void resetTitle();

  // ------------------
  // ---- Boss Bar ----
  // ------------------

  /**
   * Get if there is any way this audience can receive a boss bar.
   *
   * <p>Showing or hiding a boss bar when this method returns {@code false} will be a no-op.
   * This method returning true does not guarantee that showing a boss bar will succeed.</p>
   *
   * @return false if boss bars cannot be received by this source
   */
  boolean canShowBossBar();

  /**
   * Shows a bossbar.
   *
   * @param bar the bossbar
   */
  void showBossBar(final @NonNull BossBar bar);

  /**
   * Hides a bossbar.
   *
   * @param bar the bossbar
   */
  void hideBossBar(final @NonNull BossBar bar);

  // ----------------
  // ---- Sounds ----
  // ----------------

  /**
   * Get if there is any way this audience can receive a sound.
   *
   * <p>Playing or stopping a sound when this method returns {@code false} will be a no-op.
   * This method returning true does not guarantee that playing or stopping a sound will succeed.</p>
   *
   * @return false if sounds cannot be received by this source
   */
  boolean canPlaySound();

  /**
   * Plays a sound.
   *
   * @param sound the sound
   */
  void playSound(final @NonNull Sound sound);

  /**
   * Plays a sound.
   *
   * @param sound the sound
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   */
  void playSound(final @NonNull Sound sound, final double x, final double y, final double z);

  /**
   * Stops all sounds.
   *
   * @param stop the stop
   */
  void stopSound(final @NonNull SoundStop stop);

  // -------------------
  // ---- Inventory ----
  // -------------------

  /**
   * Get if there is any way this audience can receive a book.
   *
   * <p>Opening a book when this method returns {@code false} will be a no-op.
   * This method returning true does not guarantee that opening a book will succeed.</p>
   *
   * @return false if books cannot be received by this source
   */
  boolean canOpenBook();

  /**
   * Opens a book.
   *
   * <p>Opens a virtual book for the client, no item will be persisted.</p>
   *
   * @param book the book
   */
  void openBook(final @NonNull Book book);
}

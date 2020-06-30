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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.function.Consumer;

/**
 * An audience that delegates to a single viewer.
 */
@FunctionalInterface
public interface ForwardingAudience extends Audience {
  /**
   * Gets the delegate viewer.
   *
   * @return the viewer, or {@code null} to silently drop
   */
  @Nullable Viewer viewer();

  /**
   * Forwards the given {@code action} onto the delegate viewer, and returns the result
   * of calling {@code perform} on the delegate, or an empty audience if {@link #viewer()}
   * returned null.
   *
   * @param type the type of viewer the action requires
   * @param action the action
   * @param <T> the type of viewer
   * @return an audience
   */
  @Override
  default <T extends Viewer> @NonNull Audience perform(final @NonNull Class<T> type, final @NonNull Consumer<T> action) {
    final /* @Nullable */ Viewer viewer = this.viewer();
    if(viewer == null) {
      return Audience.empty();
    }
    final Audience result = viewer.perform(type, action);
    if(result instanceof ForwardingAudience && ((ForwardingAudience) result).viewer() == viewer) {
      return this;
    }
    return result;
  }
}

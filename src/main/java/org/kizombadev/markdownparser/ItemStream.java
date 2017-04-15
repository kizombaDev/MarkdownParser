/*
 * Marcel Swoboda
 * Copyright (C) 2017
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package org.kizombadev.markdownparser;


import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ItemStream<T> {
    private int index = 0;
    private ImmutableList<T> inputCollection;

    private ItemStream() {
    }

    public static <T> ItemStream create(ImmutableList<T> inputCollection) {
        checkNotNull(inputCollection);

        ItemStream result = new ItemStream();
        result.inputCollection = inputCollection;
        return result;
    }

    @Nullable
    public T current() {
        if (index >= inputCollection.size()) {
            return null;
        }
        return inputCollection.get(index);
    }

    @Nullable
    public T next() {
        if (index + 1 >= inputCollection.size()) {
            return null;
        }
        return inputCollection.get(index + 1);
    }

    public void stepTokenForward() {

        index++;
    }
}
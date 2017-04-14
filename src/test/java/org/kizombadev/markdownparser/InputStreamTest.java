/*
 * Marcel Swoboda
 * Copyright (C) 2017
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InputStreamTest {

    @Test
    public void testEmptyInput() {
        final InputStream tokenStream = InputStream.create("");

        assertThat(tokenStream.hasNext()).isFalse();

        assertThatThrownBy(tokenStream::next).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(tokenStream::showNext).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testOneCharacterInput() {
        InputStream tokenStream = InputStream.create("a");

        assertThat(tokenStream.hasNext()).isTrue();
        assertThat(tokenStream.showNext()).isEqualTo('a');
        assertThat(tokenStream.next()).isEqualTo('a');

        assertThat(tokenStream.hasNext()).isFalse();

        assertThatThrownBy(tokenStream::showNext).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testLargeInput() {
        InputStream tokenStream = InputStream.create("abc");
        assertThat(tokenStream.next()).isEqualTo('a');
        assertThat(tokenStream.next()).isEqualTo('b');
        assertThat(tokenStream.next()).isEqualTo('c');
    }
}

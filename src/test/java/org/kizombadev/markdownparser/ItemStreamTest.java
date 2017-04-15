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

import org.junit.Test;
import org.kizombadev.markdownparser.utils.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemStreamTest {

    @Test
    public void testEmptyInput() {
        final ItemStream tokenStream = ItemStream.create(StringUtils.convertToCharacterArray(""));
        assertThat(tokenStream.current()).isNull();
        assertThat(tokenStream.next()).isNull();
    }

    @Test
    public void testOneCharacterInput() {
        ItemStream tokenStream = ItemStream.create(StringUtils.convertToCharacterArray("a"));
        assertThat(tokenStream.current()).isEqualTo('a');
        assertThat(tokenStream.next()).isNull();
        tokenStream.stepTokenForward();
        assertThat(tokenStream.current()).isNull();
        assertThat(tokenStream.next()).isNull();
    }

    @Test
    public void testLargeInput() {
        ItemStream tokenStream = ItemStream.create(StringUtils.convertToCharacterArray("abc"));
        assertThat(tokenStream.current()).isEqualTo('a');
        tokenStream.stepTokenForward();
        assertThat(tokenStream.current()).isEqualTo('b');
        tokenStream.stepTokenForward();
        assertThat(tokenStream.current()).isEqualTo('c');
    }
}

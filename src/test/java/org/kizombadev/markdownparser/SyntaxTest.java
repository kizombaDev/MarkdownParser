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
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;

import static org.kizombadev.markdownparser.helper.SyntaxAssert.assertThat;


public class SyntaxTest {
    @Test
    public void testCreate() {
        Syntax underTest = Syntax.create(SyntaxType.Quotation);
        assertThat(underTest).isSyntaxTypeOf(SyntaxType.Quotation);
    }

    @Test
    public void testCreateWithContent() {
        Syntax underTest = Syntax.createWithContent(SyntaxType.Text, "Foo");
        assertThat(underTest).isTextElementWith("Foo");
    }

    @Test
    public void testCreateWithChildren() {
        Syntax underTest = Syntax.createWithChildren(SyntaxType.Bold, Syntax.create(SyntaxType.Italic), Syntax.create(SyntaxType.Text));

        assertThat(underTest).isSyntaxTypeOf(SyntaxType.Bold);
        assertThat(underTest.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.Italic);
        assertThat(underTest.getChildren().get(1)).isSyntaxTypeOf(SyntaxType.Text);
    }
}

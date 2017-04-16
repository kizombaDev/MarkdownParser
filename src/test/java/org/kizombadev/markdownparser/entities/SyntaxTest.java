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

package org.kizombadev.markdownparser.entities;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kizombadev.markdownparser.testhelper.SyntaxAssert.assertThat;


public class SyntaxTest {
    @Test
    public void testCreate() {
        Syntax underTest = Syntax.create(SyntaxType.QUOTATION);
        assertThat(underTest).isSyntaxTypeOf(SyntaxType.QUOTATION);
    }

    @Test
    public void testCreateWithContent() {
        Syntax underTest = Syntax.createTextSyntax("Foo");
        assertThat(underTest).isTextElementWith("Foo");
    }

    @Test
    public void testCreateWithChildren() {
        Syntax underTest = Syntax.createWithChildren(SyntaxType.BOLD, Syntax.create(SyntaxType.ITALIC), Syntax.create(SyntaxType.TEXT));

        assertThat(underTest).isSyntaxTypeOf(SyntaxType.BOLD);
        assertThat(underTest.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.ITALIC);
        assertThat(underTest.getChildren().get(1)).isSyntaxTypeOf(SyntaxType.TEXT);
    }

    @Test
    public void testChildrenCount() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT);
        assertThat(root.getChildrenCount()).isEqualTo(0);
        root.addChild(Syntax.create(SyntaxType.PARAGRAPH));
        assertThat(root.getChildrenCount()).isEqualTo(1);
    }

    @Test
    public void testRemoveChildAt() {
        //arrange
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.create(SyntaxType.PARAGRAPH));

        //act
        root.removeChildAt(0);

        //assert
        assertThat(root.getChildrenCount()).isEqualTo(0);
    }

    @Test
    public void testGetChildAt() {
        //arrange
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.create(SyntaxType.PARAGRAPH));

        //act
        Syntax paragraph = root.getChildAt(0);

        //assert
        assertThat(paragraph.getType()).isEqualTo(SyntaxType.PARAGRAPH);
    }

    //todo add missing tests
}

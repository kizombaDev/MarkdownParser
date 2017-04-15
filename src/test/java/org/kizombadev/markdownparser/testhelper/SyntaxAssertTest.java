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

package org.kizombadev.markdownparser.testhelper;

import org.junit.Test;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;

import static org.kizombadev.markdownparser.testhelper.SyntaxAssert.assertThat;


public class SyntaxAssertTest {

    @Test
    public void testIsRootAndFirstContainerAndFirstText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.BOLD,
                Syntax.createTextSyntax("Foo")));
        assertThat(root).isRootAndFirstContainerAndFirstText(SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testIsRootAndFirstContainerAndSecondText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.BOLD,
                Syntax.create(SyntaxType.ITALIC), Syntax.createTextSyntax("Foo")));
        assertThat(root).isRootAndFirstContainerAndSecondText(SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testIsRootAndFirstContainerAndThirdText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.BOLD,
                Syntax.create(SyntaxType.ITALIC), Syntax.create(SyntaxType.ITALIC), Syntax.createTextSyntax("Foo")));
        assertThat(root).isRootAndFirstContainerAndThirdText(SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testIsRootAndSecondContainerAndFirstText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.create(SyntaxType.ITALIC),
                Syntax.createWithChildren(SyntaxType.BOLD, Syntax.createTextSyntax("Foo")));
        assertThat(root).isRootAndSecondContainerAndFirstText(SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testIsRootAndFirstContainerAndFirstContainerAndFirstText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.PARAGRAPH,
                Syntax.createWithChildren(SyntaxType.BOLD, Syntax.createTextSyntax("Foo"))));
        assertThat(root).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH,
                SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testIsRootAndFirstContainerAndSecondContainerAndFirstText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.PARAGRAPH,
                Syntax.create(SyntaxType.ITALIC), Syntax.createWithChildren(SyntaxType.BOLD,
                        Syntax.createTextSyntax("Foo"))));
        assertThat(root).isRootAndFirstContainerAndSecondContainerAndFirstText(SyntaxType.PARAGRAPH,
                SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testIsRootAndFirstContainerAndThirdContainerAndFirstText() {
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.PARAGRAPH,
                Syntax.create(SyntaxType.ITALIC), Syntax.create(SyntaxType.ITALIC),
                Syntax.createWithChildren(SyntaxType.BOLD, Syntax.createTextSyntax("Foo"))));
        assertThat(root).isRootAndFirstContainerAndThirdContainerAndFirstText(SyntaxType.PARAGRAPH,
                SyntaxType.BOLD, "Foo");
    }
}
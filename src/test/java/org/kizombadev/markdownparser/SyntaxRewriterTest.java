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

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;

import static org.kizombadev.markdownparser.testhelper.SyntaxAssert.assertThat;

public class SyntaxRewriterTest {

    private SyntaxRewriter underTest;

    @Before
    public void init() {
        underTest = SyntaxRewriter.create();
    }

    @Test
    public void testCombineParagraphs() {
        Syntax syntax = underTest.rewrite(Syntax.createWithChildren(SyntaxType.ROOT,
                Syntax.createWithChildren(SyntaxType.PARAGRAPH, Syntax.createTextSyntax("Foo")),
                Syntax.createWithChildren(SyntaxType.PARAGRAPH, Syntax.createTextSyntax("Bar"))));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondText(SyntaxType.PARAGRAPH, " ");

        assertThat(syntax).isRootAndFirstContainerAndThirdText(SyntaxType.PARAGRAPH, "Bar");
    }

    @Test
    public void testRemoveMultipleBlanks() {
        Syntax syntax = underTest.rewrite(Syntax.createWithChildren(SyntaxType.ROOT,
                Syntax.createWithChildren(SyntaxType.PARAGRAPH, Syntax.createTextSyntax("Foo"),
                        Syntax.createTextSyntax(" "),
                        Syntax.createTextSyntax(" "),
                        Syntax.createTextSyntax("Bar"))));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondText(SyntaxType.PARAGRAPH, " ");
        assertThat(syntax).isRootAndFirstContainerAndThirdText(SyntaxType.PARAGRAPH, "Bar");
    }
}

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

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlGeneratorTest {
    private static final String HTML_START = "<!DOCTYPE html><html><body>";
    private static final String HTML_END = "</body></html>";
    private HtmlGenerator generator;

    @Before
    public void init() {
        generator = HtmlGenerator.create();
    }

    @Test
    public void testBigHeadline() {
        assertElementWithOneTextChild(SyntaxType.BIG_HEADLINE, "h1");
    }

    @Test
    public void testSmallHeadline() {
        assertElementWithOneTextChild(SyntaxType.SMALL_HEADLINE, "h2");
    }

    @Test
    public void testItalic() {
        assertElementWithOneTextChild(SyntaxType.ITALIC, "i");
    }

    @Test
    public void testBold() {
        assertElementWithOneTextChild(SyntaxType.BOLD, "b");
    }

    @Test
    public void testQuotation() {
        assertElementWithOneTextChild(SyntaxType.QUOTATION, "blockquote");
    }

    @Test
    public void testUnorderedList() {
        //arrange
        Syntax fooItem = Syntax.createWithChildren(SyntaxType.UNORDERED_LIST_ITEM, Syntax.createWithContent(SyntaxType.TEXT, "Foo"));
        Syntax barItem = Syntax.createWithChildren(SyntaxType.UNORDERED_LIST_ITEM, Syntax.createWithContent(SyntaxType.TEXT, "Bar"));
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.UNORDERED_LIST, fooItem, barItem));

        //act
        String html = generator.parse(root);

        //assert
        assertThat(html).isEqualTo(HTML_START + "<ul><li>Foo</li><li>Bar</li></ul>" + HTML_END);
    }

    @Test
    public void testUnorderedListWithBoldText() {
        //arrange
        Syntax boldText = Syntax.createWithChildren(SyntaxType.BOLD, Syntax.createWithContent(SyntaxType.TEXT, "Bold"));
        Syntax fooItem = Syntax.createWithChildren(SyntaxType.UNORDERED_LIST_ITEM, Syntax.createWithContent(SyntaxType.TEXT, "Foo"), boldText);
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(SyntaxType.UNORDERED_LIST, fooItem));

        //act
        String html = generator.parse(root);

        //assert
        assertThat(html).isEqualTo(HTML_START + "<ul><li>Foo<b>Bold</b></li></ul>" + HTML_END);
    }

    private void assertElementWithOneTextChild(SyntaxType type, String htmlTag) {
        //arrange
        Syntax root = Syntax.createWithChildren(SyntaxType.ROOT, Syntax.createWithChildren(type, Syntax.createWithContent(SyntaxType.TEXT, "Foo")));

        //act
        String html = generator.parse(root);

        //assert
        assertThat(html).isEqualTo(HTML_START + "<" + htmlTag + ">Foo</" + htmlTag + ">" + HTML_END);
    }
}

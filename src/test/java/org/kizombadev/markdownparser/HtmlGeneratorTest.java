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
import org.kizombadev.markdownparser.entities.*;
import org.kizombadev.markdownparser.entities.interfaces.MutableSyntax;

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
        assertElementWithOneTextChild(new BigHeadline(), "h1");
    }

    @Test
    public void testSmallHeadline() {
        assertElementWithOneTextChild(new SmallHeadline(), "h2");
    }

    @Test
    public void testItalic() {
        assertElementWithOneTextChild(new ItalicSyntax(), "i");
    }

    @Test
    public void testBold() {
        assertElementWithOneTextChild(new BoldSyntax(), "b");
    }

    @Test
    public void testQuotation() {
        assertElementWithOneTextChild(new QuotationSyntax(), "blockquote");
    }

    private void assertElementWithOneTextChild(MutableSyntax syntax, String htmlTag) {
        //arrange
        syntax.addChild(new TextSyntax("Foo"));
        RootSyntax root = new RootSyntax();
        root.addChild(syntax);

        //act
        String html = generator.parse(root);

        //assert
        assertThat(html).isEqualTo(HTML_START + "<" + htmlTag + ">Foo</" + htmlTag + ">" + HTML_END);
    }
}

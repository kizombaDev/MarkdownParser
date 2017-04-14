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

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.kizombadev.markdownparser.helper.HtmlHelper.HTML_END;
import static org.kizombadev.markdownparser.helper.HtmlHelper.HTML_START;

public class MarkdownParserTest {
    @Test
    public void test() throws IOException {
        InputStream inputStream = IOUtils.toInputStream("# Foo");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MarkdownParser.create().parse(inputStream, outputStream);

        String html = new String(outputStream.toByteArray());
        assertThat(html).isEqualTo(HTML_START + "<h1>Foo</h1>" + HTML_END);
    }

    @Test
    public void test2() throws IOException {
        String marddown = "# 1. Ueberschrift\n" +
                "## 2. Ueberschrift\n" +
                "**Fetter Text** Test \n" +
                "*Kursiver Text* Test\n" +
                "> Das ist ein Zitat\n" +
                "* Punkt 1 \n" +
                "* Punkt 2\n" +
                "* Punkt 3 **Fett** *Kusiv*";

        InputStream inputStream = IOUtils.toInputStream(marddown);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MarkdownParser.create().parse(inputStream, outputStream);

        String html = new String(outputStream.toByteArray());
    }
}

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
import org.apache.commons.io.IOUtils;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MarkdownParser {

    private MarkdownParser() {
    }

    public static MarkdownParser create() {
        return new MarkdownParser();
    }

    public void parse(InputStream inputStream, OutputStream outputStream) throws IOException {
        String input = IOUtils.toString(inputStream, "UTF-8");

        ImmutableList<Token> tokens = LexicalAnalyzer.create().parse(input);
        Syntax root = SyntaxAnalyzer.create().parse(tokens);
        String html = HtmlGenerator.create().parse(root);

        outputStream.write(html.getBytes("UTF-8"));
    }
}

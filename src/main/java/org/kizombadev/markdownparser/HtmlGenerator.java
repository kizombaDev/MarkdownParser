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

import org.jetbrains.annotations.NotNull;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;
import org.kizombadev.markdownparser.exceptions.UnknownSyntaxTypeException;

import static com.google.common.base.Preconditions.checkNotNull;

public class HtmlGenerator {

    private static final String HTML_START = "<!DOCTYPE html><html><body>";
    private static final String HTML_END = "</body></html>";

    private HtmlGenerator() {
    }

    @NotNull
    public static HtmlGenerator create() {
        return new HtmlGenerator();
    }

    public String parse(Syntax root) {
        checkNotNull(root);

        StringBuilder html = new StringBuilder();

        html.append(HTML_START);
        handle(root, html);
        html.append(HTML_END);

        return html.toString();
    }

    private void handle(Syntax root, StringBuilder html) {
        for (Syntax child : root.getChildren()) {
            if (SyntaxType.BIG_HEADLINE.equals(child.getType())) {
                handleTag("h1", child, html);
            } else if (SyntaxType.SMALL_HEADLINE.equals(child.getType())) {
                handleTag("h2", child, html);
            } else if (SyntaxType.TEXT.equals(child.getType())) {
                html.append(child.getContent());
            } else if (SyntaxType.BOLD.equals(child.getType())) {
                handleTag("b", child, html);
            } else if (SyntaxType.ITALIC.equals(child.getType())) {
                handleTag("i", child, html);
            } else if (SyntaxType.QUOTATION.equals(child.getType())) {
                handleTag("blockquote", child, html);
            } else if (SyntaxType.UNORDERED_LIST.equals(child.getType())) {
                handleTag("ul", child, html);
            } else if (SyntaxType.UNORDERED_LIST_ITEM.equals(child.getType())) {
                handleTag("li", child, html);
            } else if (SyntaxType.PARAGRAPH.equals(child.getType())) {
                handleTag("p", child, html);
            } else {
                throw new UnknownSyntaxTypeException("The syntax type " + child.getType().toString() + "is unknown");
            }
        }
    }

    private void handleTag(String htmlTag, Syntax child, StringBuilder html) {
        html.append("<");
        html.append(htmlTag);
        html.append(">");

        handle(child, html);

        html.append("</");
        html.append(htmlTag);
        html.append(">");
    }
}

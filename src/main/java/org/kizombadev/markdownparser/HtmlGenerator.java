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

import org.kizombadev.markdownparser.entities.*;
import org.kizombadev.markdownparser.entities.interfaces.ImmutableSyntax;

public class HtmlGenerator {

    private static final String HTML_START = "<!DOCTYPE html><html><body>";
    private static final String HTML_END = "</body></html>";

    private HtmlGenerator() {
    }

    public static HtmlGenerator create() {
        return new HtmlGenerator();
    }

    public String parse(ImmutableSyntax root) {

        StringBuilder html = new StringBuilder();

        html.append(HTML_START);
        handle(root, html);
        html.append(HTML_END);

        return html.toString();
    }

    private void handle(ImmutableSyntax root, StringBuilder html) {
        for (ImmutableSyntax child : root.getChildren()) {
            if (child instanceof BigHeadline) {
                handleTag("h1", child, html);
            } else if (child instanceof SmallHeadline) {
                handleTag("h2", child, html);
            } else if (child instanceof TextSyntax) {
                html.append(child.convertTo(TextSyntax.class).getText());
            } else if (child instanceof BoldSyntax) {
                handleTag("b", child, html);
            } else if (child instanceof ItalicSyntax) {
                handleTag("i", child, html);
            } else if (child instanceof QuotationSyntax) {
                handleTag("blockquote", child, html);
            }
        }
    }

    private void handleTag(String htmlTag, ImmutableSyntax child, StringBuilder html) {
        html.append("<" + htmlTag + ">");
        handle(child, html);
        html.append("</" + htmlTag + ">");
    }
}

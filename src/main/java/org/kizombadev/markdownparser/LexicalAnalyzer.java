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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.kizombadev.markdownparser.entities.Token;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LexicalAnalyzer {
    private final List<Token> tokens = new ArrayList<>();

    private StringBuilder text = new StringBuilder();

    @NotNull
    public static LexicalAnalyzer create() {
        return new LexicalAnalyzer();
    }

    @NotNull
    public ImmutableList<Token> parse(final String input) {
        checkNotNull(input);

        final ParserInputStream tokenStream = ParserInputStream.create(input);

        while (tokenStream.hasNext()) {
            tokenStream.next();

            if (tokenStream.current() == '\r' && tokenStream.hasNext() && tokenStream.showNext() == '\n') {
                handleEndOfText();
                tokens.add(Token.NewLine);
                tokenStream.next();
            } else if (tokenStream.current() == '\n') {
                handleEndOfText();
                tokens.add(Token.NewLine);
            } else if (tokenStream.current() == '#' && tokenStream.hasNext() && tokenStream.showNext() == '#') {
                handleEndOfText();
                tokens.add(Token.DoubleNumberSign);
                tokenStream.next();
            } else if (tokenStream.current() == '#') {
                handleEndOfText();
                tokens.add(Token.NumberSign);
            } else if (tokenStream.current() == '*' && tokenStream.hasNext() && tokenStream.showNext() == '*') {
                handleEndOfText();
                tokens.add(Token.DoubleStar);
                tokenStream.next();
            } else if (tokenStream.current() == '*') {
                handleEndOfText();
                tokens.add(Token.Star);
            } else if (tokenStream.current() == '>') {
                handleEndOfText();
                tokens.add(Token.GreaterThanSign);
            } else if (tokenStream.current() == ' ') {
                handleEndOfText();
                tokens.add(Token.Blank);
            } else {
                text.append(tokenStream.current());
            }
        }

        handleEndOfText();

        return ImmutableList.copyOf(tokens);
    }

    private void handleEndOfText() {
        if (text.length() == 0) {
            return;
        }

        String currentText = text.toString();

        if (Strings.isNullOrEmpty(currentText)) {
            return;
        }

        tokens.add(Token.createTextToken(currentText));
        text = new StringBuilder();
    }
}
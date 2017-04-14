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

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kizombadev.markdownparser.entities.*;
import org.kizombadev.markdownparser.entities.interfaces.ImmutableSyntax;
import org.kizombadev.markdownparser.entities.interfaces.MutableSyntax;

//todo make the syntaxTree Immutable

public class SyntaxAnalyzer {

    private ImmutableList<Token> tokens;
    private int tokenIndex = 0;
    private boolean isBoldModeActive = false;
    private boolean isItalicModeEnabled = false;

    @NotNull
    public static SyntaxAnalyzer create() {
        return new SyntaxAnalyzer();
    }

    public ImmutableSyntax parse(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        MutableSyntax root = new RootSyntax();

        Token currentToken = currentToken();

        while (currentToken != null) {

            if (Token.NumberSign.equals(currentToken)) {
                handleBigHeadlineLine(root);
            } else if (Token.DoubleNumberSign.equals(currentToken)) {
                handleSmallHeadlineLine(root);
            } else if (Token.GreaterThanSign.equals(currentToken)) {
                handleQuotation(root);
            } else if (Token.DoubleStar.equals(currentToken)) {
                handleBold(root);
            } else if (Token.Star.equals(currentToken) && Token.Blank.equals(nextToken())) {
                handleUnorderedList(root);
            } else if (Token.Star.equals(currentToken)) {
                handleItalic(root);
            } else if (Token.NewLine.equals(currentToken)) {
                stepTokenForward();
            }

            currentToken = currentToken();
        }

        return root;
    }

    private void handleUnorderedList(MutableSyntax currentRoot) {
        MutableSyntax unorderedList = new UnorderedListSyntax();

        while (Token.Star.equals(currentToken()) && Token.Blank.equals(nextToken())) {
            stepTokenForward();
            stepTokenForward();

            handleUnorderedListItem(unorderedList);

            if (Token.NewLine.equals(currentToken())) {
                stepTokenForward();
            }
        }

        currentRoot.addChild(unorderedList);
    }

    private void handleUnorderedListItem(MutableSyntax unorderedList) {
        MutableSyntax unorderedListItem = new UnorderedListItemSyntax();
        handleLineContainer(unorderedListItem);
        unorderedList.addChild(unorderedListItem);
    }

    private void handleQuotation(MutableSyntax currentRoot) {
        MutableSyntax quotation = new QuotationSyntax();
        stepTokenForward();

        handleLineContainer(quotation);
        currentRoot.addChild(quotation);
    }

    private void handleBigHeadlineLine(MutableSyntax currentRoot) {
        MutableSyntax bigHeadline = new BigHeadline();
        stepTokenForward();

        handleLineContainer(bigHeadline);
        currentRoot.addChild(bigHeadline);
    }

    private void handleSmallHeadlineLine(MutableSyntax currentRoot) {
        MutableSyntax smallHeadline = new SmallHeadline();
        stepTokenForward();


        handleLineContainer(smallHeadline);
        currentRoot.addChild(smallHeadline);
    }

    private void handleLineContainer(MutableSyntax currentRoot) {

        Token currentToken = currentToken();

        while (currentToken != null && !currentToken.equals(Token.NewLine)) {

            if (Token.DoubleStar.equals(currentToken) && isBoldModeActive ||
                    Token.Star.equals(currentToken) && isItalicModeEnabled) {
                return;
            } else if (Token.DoubleStar.equals(currentToken)) {
                handleBold(currentRoot);
            } else if (Token.Star.equals(currentToken)) {
                handleItalic(currentRoot);
            } else if (currentToken.isTextToken()) {
                currentRoot.addChild(new TextSyntax(currentToken.getTextValue()));
                stepTokenForward();
            }

            currentToken = currentToken();
        }
    }

    private void handleBold(MutableSyntax currentRoot) {
        isBoldModeActive = true;
        stepTokenForward();
        MutableSyntax newSyntaxElement = new BoldSyntax();
        handleLineContainer(newSyntaxElement);
        stepTokenForward();
        currentRoot.addChild(newSyntaxElement);
        isBoldModeActive = false;
    }

    private void handleItalic(MutableSyntax currentRoot) {
        isItalicModeEnabled = true;
        stepTokenForward();
        MutableSyntax newSyntaxElement = new ItalicSyntax();
        handleLineContainer(newSyntaxElement);
        stepTokenForward();
        currentRoot.addChild(newSyntaxElement);
        isItalicModeEnabled = false;
    }

    @Nullable
    private Token currentToken() {
        if (tokenIndex >= tokens.size()) {
            return null;
        }
        return tokens.get(tokenIndex);
    }

    @Nullable
    private Token nextToken() {
        if (tokenIndex + 1 >= tokens.size()) {
            return null;
        }
        return tokens.get(tokenIndex + 1);
    }

    private void stepTokenForward() {
        tokenIndex++;
    }
}

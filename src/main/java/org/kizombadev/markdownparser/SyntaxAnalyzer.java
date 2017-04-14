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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;
import org.kizombadev.markdownparser.entities.Token;
import org.kizombadev.markdownparser.exceptions.MarkdownParserException;

//todo make the syntaxTree Immutable

public class SyntaxAnalyzer {

    private static final int INFINITY_LOOP_DETECTION_COUNT = 10;
    private ImmutableList<Token> tokens;
    private int tokenIndex = 0;
    private boolean isBoldModeActive = false;
    private boolean isItalicModeEnabled = false;
    private int blankCounter = 0;
    private int infinityLoopCounter = 0;

    @NotNull
    public static SyntaxAnalyzer create() {
        return new SyntaxAnalyzer();
    }

    public Syntax parse(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        Syntax root = Syntax.create(SyntaxType.ROOT);

        Token currentToken = currentToken();

        while (currentToken != null) {

            if (Token.NumberSign.equals(currentToken)) {
                handleBigHeadlineLine(root);
            } else if (Token.DoubleNumberSign.equals(currentToken)) {
                handleSmallHeadlineLine(root);
            } else if (Token.GreaterThanSign.equals(currentToken)) {
                handleQuotation(root);
            } else if (Token.Star.equals(currentToken) && Token.Blank.equals(nextToken())) {
                handleUnorderedList(root);
            } else if (Token.NewLine.equals(currentToken)) {
                stepTokenForward();
                blankCounter = 0;
            } else {
                handleParagraph(root);
            }

            checkInfinityLoop();

            currentToken = currentToken();
        }

        return root;
    }

    private void handleLineContainer(Syntax currentRoot) {
        skipBlanks();

        Token currentToken = currentToken();

        while (currentToken != null && !currentToken.equals(Token.NewLine)) {

            if ((Token.DoubleStar.equals(currentToken) && isBoldModeActive) ||
                    (Token.Star.equals(currentToken) && isItalicModeEnabled)) {
                return;
            } else if (Token.DoubleStar.equals(currentToken)) {
                handleBold(currentRoot);
            } else if (Token.Star.equals(currentToken)) {
                handleItalic(currentRoot);
            } else if (currentToken.isTextToken()) {
                currentRoot.addChild(Syntax.createWithContent(SyntaxType.TEXT, addBlanks() + currentToken.getTextValue()));
                stepTokenForward();
            } else if (Token.Blank.equals(currentToken)) {
                stepTokenForward();
                blankCounter++;
            }

            checkInfinityLoop();

            currentToken = currentToken();
        }
    }

    private void checkInfinityLoop() {
        if (infinityLoopCounter > INFINITY_LOOP_DETECTION_COUNT) {
            throw new MarkdownParserException();
        }

        infinityLoopCounter++;
    }

    private void skipBlanks() {
        Token currentToken = currentToken();

        while (Token.Blank.equals(currentToken)) {
            stepTokenForward();
            currentToken = currentToken();
        }
    }

    private String addBlanks() {

        StringBuilder blanks = new StringBuilder();

        for (int i = 0; i < blankCounter; i++) {
            blanks.append(" ");
        }

        blankCounter = 0;

        return blanks.toString();
    }

    private void handleParagraph(Syntax root) {
        Syntax paragraph = Syntax.create(SyntaxType.PARAGRAPH);
        handleLineContainer(paragraph);
        root.addChild(paragraph);
    }

    private void handleUnorderedList(Syntax currentRoot) {
        Syntax unorderedList = Syntax.create(SyntaxType.UNORDERED_LIST);

        while (Token.Star.equals(currentToken()) && Token.Blank.equals(nextToken())) {
            stepTokenForward();
            stepTokenForward();

            handleUnorderedListItem(unorderedList);

            if (Token.NewLine.equals(currentToken())) {
                stepTokenForward();
                blankCounter = 0;
            }
        }

        currentRoot.addChild(unorderedList);
    }

    private void handleUnorderedListItem(Syntax unorderedList) {
        Syntax unorderedListItem = Syntax.create(SyntaxType.UNORDERED_LIST_ITEM);
        handleLineContainer(unorderedListItem);
        unorderedList.addChild(unorderedListItem);
    }

    private void handleQuotation(Syntax currentRoot) {
        Syntax quotation = Syntax.create(SyntaxType.QUOTATION);
        stepTokenForward();

        handleLineContainer(quotation);
        currentRoot.addChild(quotation);
    }

    private void handleBigHeadlineLine(Syntax currentRoot) {
        Syntax bigHeadline = Syntax.create(SyntaxType.BIG_HEADLINE);
        stepTokenForward();

        handleLineContainer(bigHeadline);
        currentRoot.addChild(bigHeadline);
    }

    private void handleSmallHeadlineLine(Syntax currentRoot) {
        Syntax smallHeadline = Syntax.create(SyntaxType.SMALL_HEADLINE);
        stepTokenForward();


        handleLineContainer(smallHeadline);
        currentRoot.addChild(smallHeadline);
    }

    private void handleBold(Syntax currentRoot) {
        isBoldModeActive = true;
        stepTokenForward();
        Syntax newSyntaxElement = Syntax.create(SyntaxType.BOLD);
        handleLineContainer(newSyntaxElement);
        stepTokenForward();
        currentRoot.addChild(newSyntaxElement);
        isBoldModeActive = false;
    }

    private void handleItalic(Syntax currentRoot) {
        isItalicModeEnabled = true;
        stepTokenForward();
        Syntax newSyntaxElement = Syntax.create(SyntaxType.ITALIC);
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
        infinityLoopCounter = 0;
    }
}

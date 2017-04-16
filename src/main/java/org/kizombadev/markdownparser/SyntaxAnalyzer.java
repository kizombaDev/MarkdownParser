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

import static com.google.common.base.Preconditions.checkNotNull;

public class SyntaxAnalyzer {

    private static final int INFINITY_LOOP_DETECTION_COUNT = 10;
    private boolean isBoldModeActive = false;
    private boolean isItalicModeEnabled = false;
    private boolean shouldInsertBlank = false;
    private int infinityLoopCounter = 0;
    private ItemStream<Token> tokenStream;

    @NotNull
    public static SyntaxAnalyzer create() {
        return new SyntaxAnalyzer();
    }

    public Syntax parse(ImmutableList<Token> tokens) {
        checkNotNull(tokens);
        this.tokenStream = ItemStream.create(tokens);

        Syntax root = Syntax.create(SyntaxType.ROOT);

        Token currentToken = current();

        while (currentToken != null) {

            if (Token.NumberSign.equals(currentToken)) {
                handleBigHeadlineLine(root);
            } else if (Token.DoubleNumberSign.equals(currentToken)) {
                handleSmallHeadlineLine(root);
            } else if (Token.GreaterThanSign.equals(currentToken)) {
                handleQuotation(root);
            } else if (Token.Star.equals(currentToken) && Token.Blank.equals(next())) {
                handleUnorderedList(root);
            } else if (Token.NewLine.equals(currentToken)) {
                stepTokenForward();
                shouldInsertBlank = false;
            } else {
                handleParagraph(root);
            }

            checkInfinityLoop();

            currentToken = current();
        }

        SyntaxRewriter.create().rewrite(root);

        return root;
    }

    private void handleLine(Syntax currentRoot) {
        skipBlanks();

        Token currentToken = current();

        while (currentToken != null && !currentToken.equals(Token.NewLine)) {

            if ((Token.DoubleStar.equals(currentToken) && isBoldModeActive) ||
                    (Token.Star.equals(currentToken) && isItalicModeEnabled)) {
                return;
            } else if (Token.DoubleStar.equals(currentToken)) {
                handleBlank(currentRoot);
                handleBold(currentRoot);
            } else if (Token.Star.equals(currentToken)) {
                handleBlank(currentRoot);
                handleItalic(currentRoot);
            } else if (currentToken.isTextToken()) {
                handleBlank(currentRoot);
                currentRoot.addChild(Syntax.createTextSyntax(currentToken.getTextValue()));
                stepTokenForward();
            } else if (Token.Blank.equals(currentToken)) {
                //currentRoot.addChild(Syntax.createTextSyntax(" "));
                stepTokenForward();
                shouldInsertBlank = true;
            }

            checkInfinityLoop();

            currentToken = current();
        }
    }

    private void handleBlank(Syntax currentRoot) {
        if (shouldInsertBlank) {
            currentRoot.addChild(Syntax.createTextSyntax(" "));
        }
        shouldInsertBlank = false;
    }

    private void checkInfinityLoop() {
        if (infinityLoopCounter > INFINITY_LOOP_DETECTION_COUNT) {
            throw new MarkdownParserException();
        }

        infinityLoopCounter++;
    }

    private void skipBlanks() {
        Token currentToken = current();

        while (Token.Blank.equals(currentToken)) {
            stepTokenForward();
            currentToken = current();
        }
    }

    private void handleParagraph(Syntax root) {
        Syntax paragraph = Syntax.create(SyntaxType.PARAGRAPH);
        handleLine(paragraph);
        root.addChild(paragraph);
    }

    private void handleUnorderedList(Syntax currentRoot) {
        Syntax unorderedList = Syntax.create(SyntaxType.UNORDERED_LIST);

        while (Token.Star.equals(current()) && Token.Blank.equals(next())) {
            stepTokenForward();
            stepTokenForward();

            handleUnorderedListItem(unorderedList);

            if (Token.NewLine.equals(current())) {
                stepTokenForward();
                shouldInsertBlank = false;
            }
        }

        currentRoot.addChild(unorderedList);
    }

    private void handleUnorderedListItem(Syntax unorderedList) {
        Syntax unorderedListItem = Syntax.create(SyntaxType.UNORDERED_LIST_ITEM);
        handleLine(unorderedListItem);
        unorderedList.addChild(unorderedListItem);
    }

    private void handleQuotation(Syntax currentRoot) {
        Syntax quotation = Syntax.create(SyntaxType.QUOTATION);
        stepTokenForward();

        handleLine(quotation);
        currentRoot.addChild(quotation);
    }

    private void handleBigHeadlineLine(Syntax currentRoot) {
        Syntax bigHeadline = Syntax.create(SyntaxType.BIG_HEADLINE);
        stepTokenForward();

        handleLine(bigHeadline);
        currentRoot.addChild(bigHeadline);
    }

    private void handleSmallHeadlineLine(Syntax currentRoot) {
        Syntax smallHeadline = Syntax.create(SyntaxType.SMALL_HEADLINE);
        stepTokenForward();

        handleLine(smallHeadline);
        currentRoot.addChild(smallHeadline);
    }

    private void handleBold(Syntax currentRoot) {
        isBoldModeActive = true;
        stepTokenForward();
        Syntax newSyntaxElement = Syntax.create(SyntaxType.BOLD);
        handleLine(newSyntaxElement);
        stepTokenForward();
        currentRoot.addChild(newSyntaxElement);
        isBoldModeActive = false;
    }

    private void handleItalic(Syntax currentRoot) {
        isItalicModeEnabled = true;
        stepTokenForward();
        Syntax newSyntaxElement = Syntax.create(SyntaxType.ITALIC);
        handleLine(newSyntaxElement);
        stepTokenForward();
        currentRoot.addChild(newSyntaxElement);
        isItalicModeEnabled = false;
    }

    @Nullable
    private Token current() {
        return tokenStream.current();
    }

    @Nullable
    private Token next() {
        return tokenStream.next();
    }

    private void stepTokenForward() {
        tokenStream.stepTokenForward();
        infinityLoopCounter = 0;
    }
}

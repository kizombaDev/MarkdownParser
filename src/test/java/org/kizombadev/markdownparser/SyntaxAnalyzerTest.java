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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.Syntax;
import org.kizombadev.markdownparser.entities.SyntaxType;
import org.kizombadev.markdownparser.entities.Token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kizombadev.markdownparser.testhelper.SyntaxAssert.assertThat;

public class SyntaxAnalyzerTest {
    private SyntaxAnalyzer underTest;

    @Before
    public void init() {
        underTest = SyntaxAnalyzer.create();
    }

    @Test
    public void testNumberSignText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.BIG_HEADLINE, "Foo");
    }

    @Test
    public void testDoubleNumberSignText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleNumberSign, Token.createTextToken("Foo")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.SMALL_HEADLINE, "Foo");
    }

    @Test
    public void testDoubleNumberSignBlankText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleNumberSign, Token.Blank, Token.createTextToken("Foo")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.SMALL_HEADLINE, "Foo");
    }

    @Test
    public void testNumberSignTextNewLineDoubleNumberSingText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo"), Token.NewLine, Token.DoubleNumberSign, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(2);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.BIG_HEADLINE, "Foo");
        assertThat(syntax).isRootAndSecondContainerAndFirstText(SyntaxType.SMALL_HEADLINE, "Bar");
    }

    @Test
    public void testNumberSignTextDoubleStartTextDoubleStar() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo"), Token.DoubleStar, Token.createTextToken("Bar"), Token.DoubleStar));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.BIG_HEADLINE, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondContainerAndFirstText(SyntaxType.BIG_HEADLINE, SyntaxType.BOLD, "Bar");
    }

    @Test
    public void testGreaterThanSignText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.GreaterThanSign, Token.createTextToken("Foo")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.QUOTATION, "Foo");
    }

    @Test
    public void testDoubleStarTextDoubleStar() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleStar, Token.createTextToken("Foo"), Token.DoubleStar));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, SyntaxType.BOLD, "Foo");
    }

    @Test
    public void testStarTextStar() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.createTextToken("Foo"), Token.Star));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, SyntaxType.ITALIC, "Foo");
    }

    @Test
    public void testUnorderedListWithOneItem() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.Blank, Token.createTextToken("Foo"), Token.NewLine));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.UNORDERED_LIST, SyntaxType.UNORDERED_LIST_ITEM, "Foo");
    }

    @Test
    public void testUnorderedListWithTwoItems() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.Blank, Token.createTextToken("Foo"), Token.NewLine, Token.Star, Token.Blank, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.UNORDERED_LIST, SyntaxType.UNORDERED_LIST_ITEM, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondContainerAndFirstText(SyntaxType.UNORDERED_LIST, SyntaxType.UNORDERED_LIST_ITEM, "Bar");
    }

    @Test
    public void testText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.createTextToken("Foo")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, "Foo");
    }

    @Test
    public void testTextBlankBlankNewLineText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.createTextToken("Foo"), Token.Blank, Token.Blank, Token.NewLine, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondText(SyntaxType.PARAGRAPH, " ");
        assertThat(syntax).isRootAndFirstContainerAndThirdText(SyntaxType.PARAGRAPH, " ");
        assertThat(syntax).isRootAndFirstContainerAndFourthText(SyntaxType.PARAGRAPH, "Bar");
    }

    @Test
    public void test() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleStar, Token.createTextToken("Foo"), Token.DoubleStar, Token.Blank, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, SyntaxType.BOLD, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondText(SyntaxType.PARAGRAPH, " ");
        assertThat(syntax).isRootAndFirstContainerAndThirdText(SyntaxType.PARAGRAPH, "Bar");
    }


    @Test
    public void testBlankBetweenBoldAndItalicBug() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleStar, Token.createTextToken("Foo"), Token.DoubleStar, Token.Blank, Token.Star, Token.createTextToken("Bar"), Token.Star));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, SyntaxType.BOLD, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondText(SyntaxType.PARAGRAPH, " ");
        assertThat(syntax).isRootAndFirstContainerAndThirdContainerAndFirstText(SyntaxType.PARAGRAPH, SyntaxType.ITALIC, "Bar");
    }

    @Test
    public void testBlankInUnorderedListBug() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.Blank, Token.createTextToken("Foo"), Token.Blank, Token.NewLine, Token.Star, Token.Blank, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstContainerAndFirstText(SyntaxType.UNORDERED_LIST, SyntaxType.UNORDERED_LIST_ITEM, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondContainerAndFirstText(SyntaxType.UNORDERED_LIST, SyntaxType.UNORDERED_LIST_ITEM, "Bar");
    }

    @Test
    public void testMultiBlanksBug() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.createTextToken("Foo"), Token.Blank, Token.Blank, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(1);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, "Foo");
        assertThat(syntax).isRootAndFirstContainerAndSecondText(SyntaxType.PARAGRAPH, " ");
        assertThat(syntax).isRootAndFirstContainerAndThirdText(SyntaxType.PARAGRAPH, "Bar");
    }

    @Test
    public void testParagraphSeparator() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.createTextToken("Foo"), Token.NewLine, Token.NewLine, Token.createTextToken("Bar")));
        assertThat(syntax).hasChildrenCount(2);
        assertThat(syntax).isRootAndFirstContainerAndFirstText(SyntaxType.PARAGRAPH, "Foo");
        assertThat(syntax).isRootAndSecondContainerAndFirstText(SyntaxType.PARAGRAPH, "Bar");
    }

    @Test
    @Ignore
    public void testBoldAndItalicBug() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleStar, Token.Star, Token.createTextToken("Foo"), Token.DoubleStar, Token.Star));
        assertThat(syntax.getChildren()).hasSize(1);

        Syntax paragraph = syntax.getChildren().get(0);
        assertThat(paragraph).isSyntaxTypeOf(SyntaxType.PARAGRAPH);

        assertThat(paragraph.getChildren()).hasSize(1);
        assertThat(paragraph.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.BOLD);

        assertThat(paragraph.getChildren().get(0).getChildren()).hasSize(1);
        assertThat(paragraph.getChildren().get(0).getChildren().get(0)).isSyntaxTypeOf(SyntaxType.BOLD);

        assertThat(paragraph.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(1);
        assertThat(paragraph.getChildren().get(0).getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    //todo multi line
    //todo zweit zitate nacheinander
}

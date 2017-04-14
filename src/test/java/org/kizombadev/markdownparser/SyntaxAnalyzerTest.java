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
import org.junit.Before;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.SyntaxType;
import org.kizombadev.markdownparser.entities.Token;
import org.kizombadev.markdownparser.entities.interfaces.ImmutableSyntax;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kizombadev.markdownparser.helper.SyntaxAssert.assertThat;

public class SyntaxAnalyzerTest {
    private SyntaxAnalyzer underTest;

    @Before
    public void init() {
        underTest = SyntaxAnalyzer.create();
    }

    @Test
    public void testNumberSignText() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.BigHeadline);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    @Test
    public void testDoubleNumberSignText() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.DoubleNumberSign, Token.createTextToken("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.SmallHeadline);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    @Test
    public void testNumberSignTextNewLineDoubleNumberSingText() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo"), Token.NewLine, Token.DoubleNumberSign, Token.createTextToken("Bar")));
        assertThat(syntax.getChildren()).hasSize(2);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.BigHeadline);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
        assertThat(syntax.getChildren().get(1)).isSyntaxTypeOf(SyntaxType.SmallHeadline);
        assertThat(syntax.getChildren().get(1).getChildren().get(0)).isTextElementWith("Bar");

    }

    @Test
    public void testNumberSignTextDoubleStartTextDoubleStar() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo"), Token.DoubleStar, Token.createTextToken("Bar"), Token.DoubleStar));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.BigHeadline);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
        assertThat(syntax.getChildren().get(0).getChildren().get(1)).isSyntaxTypeOf(SyntaxType.Bold);
        assertThat(syntax.getChildren().get(0).getChildren().get(1).getChildren().get(0)).isTextElementWith("Bar");
    }

    @Test
    public void testGreaterThanSignText() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.GreaterThanSign, Token.createTextToken("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.Quotation);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    @Test
    public void testDoubleStarTextDoubleStar() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.DoubleStar, Token.createTextToken("Foo"), Token.DoubleStar));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.Bold);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    @Test
    public void testStarTextStar() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.createTextToken("Foo"), Token.Star));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.Italic);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    @Test
    public void testUnorderedListWithOneItem() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.Blank, Token.createTextToken("Foo"), Token.NewLine));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.UnorderedList);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isSyntaxTypeOf(SyntaxType.UnorderdListItem);
        assertThat(syntax.getChildren().get(0).getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
    }

    @Test
    public void testUnorderedListWithTwoItems() {
        ImmutableSyntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.Blank, Token.createTextToken("Foo"), Token.NewLine, Token.Star, Token.Blank, Token.createTextToken("Bar")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isSyntaxTypeOf(SyntaxType.UnorderedList);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isSyntaxTypeOf(SyntaxType.UnorderdListItem);
        assertThat(syntax.getChildren().get(0).getChildren().get(0).getChildren().get(0)).isTextElementWith("Foo");
        assertThat(syntax.getChildren().get(0).getChildren().get(1)).isSyntaxTypeOf(SyntaxType.UnorderdListItem);
        assertThat(syntax.getChildren().get(0).getChildren().get(1).getChildren().get(0)).isTextElementWith("Bar");
    }
}

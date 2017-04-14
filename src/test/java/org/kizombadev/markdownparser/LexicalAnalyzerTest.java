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
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.Token;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LexicalAnalyzerTest {
    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();
    private LexicalAnalyzer underTest;
    private String newLine = System.lineSeparator();

    @Before
    public void Init() {
        underTest = LexicalAnalyzer.create();
    }

    @Test
    public void TestMethod1() {
        assertThat(underTest.parse("#")).containsOnly(Token.NumberSign);
    }

    @Test
    public void TestMethod2() {
        assertThat(underTest.parse("##")).containsOnly(Token.DoubleNumberSign);
    }

    @Test
    public void TestMethod3() {
        assertThat(underTest.parse("# ##")).containsSequence(Token.NumberSign, Token.createTextToken(" "), Token.DoubleNumberSign);
    }

    @Test
    public void TestMethod4() {
        assertThat(underTest.parse("*")).containsOnly(Token.Star);
    }

    @Test
    public void TestMethod5() {
        assertThat(underTest.parse("**")).containsOnly(Token.DoubleStar);
    }

    @Test
    public void TestMethod6() {
        assertThat(underTest.parse(newLine)).containsOnly(Token.NewLine);
    }

    @Test
    public void TestMethod7() {
        assertThat(underTest.parse(newLine + newLine)).containsSequence(Token.NewLine, Token.NewLine);
    }

    @Test
    public void TestMethod8() {
        assertThat(underTest.parse(">")).containsOnly(Token.GreaterThanSign);
    }

    @Test
    public void TestMethod9() {
        assertThat(underTest.parse("42")).containsOnly(Token.createTextToken("42"));
    }

    @Test
    public void TestMethod10() {
        assertThat(underTest.parse(" #")).containsSequence(Token.createTextToken(" "), Token.NumberSign);
    }

    @Test
    public void TestMethod11() {
        assertThat(underTest.parse("# Foo")).containsSequence(Token.NumberSign, Token.createTextToken(" Foo"));
    }

    @Test
    public void TestMethod12() {
        assertThat(underTest.parse("# *Foo*")).containsSequence(Token.NumberSign,
                Token.createTextToken(" "),
                Token.Star,
                Token.createTextToken("Foo"),
                Token.Star);
    }

    @Test
    public void TestMethod13() {
        assertThat(underTest.parse("*Foo* **Bar**")).containsSequence(
                Token.Star,
                Token.createTextToken("Foo"),
                Token.Star,
                Token.createTextToken(" "),
                Token.DoubleStar,
                Token.createTextToken("Bar"),
                Token.DoubleStar);
    }

    @Test
    public void test() {
        String markdown = "#Das ist eine h1-Uberschrift\n" +
                "\n" +
                "##Das ist eine h2-Uberschrift\n" +
                "\n" +
                "*Erster Punkt\n" +
                "*Zweiter Punkt\n" +
                "\n" +
                "*Ein neuer Punkt\n" +
                "\n" +
                "**Das hier ist Fett**\n" +
                "*Das ist ein kursiver Text*\n" +
                "\n" +
                ">Das ist mein Zitat";

        ImmutableList<Token> expectedTokens = ImmutableList.copyOf(new Token[]{Token.NumberSign,
                Token.createTextToken("Das ist eine h1-Uberschrift"),
                Token.NewLine,
                Token.NewLine,
                Token.DoubleNumberSign,
                Token.createTextToken("Das ist eine h2-Uberschrift"),
                Token.NewLine,
                Token.NewLine,
                Token.Star,
                Token.createTextToken("Erster Punkt"),
                Token.NewLine,
                Token.Star,
                Token.createTextToken("Zweiter Punkt"),
                Token.NewLine,
                Token.NewLine,
                Token.Star,
                Token.createTextToken("Ein neuer Punkt"),
                Token.NewLine,
                Token.NewLine,
                Token.DoubleStar,
                Token.createTextToken("Das hier ist Fett"),
                Token.DoubleStar,
                Token.NewLine,
                Token.Star,
                Token.createTextToken("Das ist ein kursiver Text"),
                Token.Star,
                Token.NewLine,
                Token.NewLine,
                Token.GreaterThanSign,
                Token.createTextToken("Das ist mein Zitat")});

        ImmutableList<Token> actualTokens = underTest.parse(markdown).asList();

        softly.assertThat(actualTokens).hasSize(expectedTokens.size());

        for (int i = 0; i < expectedTokens.size(); i++) {
            softly.assertThat(actualTokens.get(i)).as("Token index is %s", i).isEqualTo(expectedTokens.get(i));
        }

    }
}

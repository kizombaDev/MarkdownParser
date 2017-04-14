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
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.Token;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LexicalAnalyzerTest {
    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();
    private final String newLine = System.lineSeparator();
    private LexicalAnalyzer underTest;

    @Before
    public void Init() {
        underTest = LexicalAnalyzer.create();
    }

    @Test
    public void testWindowsNewLine() {
        assertThat(underTest.parse("\r\n")).containsOnly(Token.NewLine);
    }

    @Test
    public void testLinuxNewLine() {
        assertThat(underTest.parse("\n")).containsOnly(Token.NewLine);
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
        assertThat(underTest.parse("# ##")).containsSequence(Token.NumberSign, Token.Blank, Token.DoubleNumberSign);
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
        assertThat(underTest.parse(" #")).containsSequence(Token.Blank, Token.NumberSign);
    }

    @Test
    public void TestMethod11() {
        assertThat(underTest.parse("# Foo")).containsSequence(Token.NumberSign, Token.Blank, Token.createTextToken("Foo"));
    }

    @Test
    public void TestMethod12() {
        assertThat(underTest.parse("# *Foo*")).containsSequence(Token.NumberSign,
                Token.Blank,
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
                Token.Blank,
                Token.DoubleStar,
                Token.createTextToken("Bar"),
                Token.DoubleStar);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void test() {
        String markdown = "#h1-Uberschrift\n" +
                "\n" +
                "##h2-Uberschrift\n" +
                "\n" +
                "* ErsterPunkt\n" +
                "* ZweiterPunkt\n" +
                "\n" +
                "* NeuerPunkt\n" +
                "\n" +
                "**Fett**\n" +
                "*KursiverText*\n" +
                "\n" +
                ">Zitat";

        ImmutableList<Token> expectedTokens = ImmutableList.copyOf(new Token[]{Token.NumberSign,
                Token.createTextToken("h1-Uberschrift"),
                Token.NewLine,
                Token.NewLine,
                Token.DoubleNumberSign,
                Token.createTextToken("h2-Uberschrift"),
                Token.NewLine,
                Token.NewLine,
                Token.Star,
                Token.Blank,
                Token.createTextToken("ErsterPunkt"),
                Token.NewLine,
                Token.Star,
                Token.Blank,
                Token.createTextToken("ZweiterPunkt"),
                Token.NewLine,
                Token.NewLine,
                Token.Star,
                Token.Blank,
                Token.createTextToken("NeuerPunkt"),
                Token.NewLine,
                Token.NewLine,
                Token.DoubleStar,
                Token.createTextToken("Fett"),
                Token.DoubleStar,
                Token.NewLine,
                Token.Star,
                Token.createTextToken("KursiverText"),
                Token.Star,
                Token.NewLine,
                Token.NewLine,
                Token.GreaterThanSign,
                Token.createTextToken("Zitat")});

        ImmutableList<Token> actualTokens = underTest.parse(markdown).asList();

        softly.assertThat(actualTokens).hasSize(expectedTokens.size());

        for (int i = 0; i < expectedTokens.size(); i++) {
            softly.assertThat(actualTokens.get(i)).as("Token index is %s", i).isEqualTo(expectedTokens.get(i));
        }

    }
}

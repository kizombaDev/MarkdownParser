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

import org.junit.Assert;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.Token;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TokenTest {

    @Test
    public void testNewLineToken() {
        Token underTest = Token.NewLine;
        assertThat(underTest).isEqualTo(Token.NewLine);
    }

    @Test
    public void testStarToken() {
        Token underTest = Token.Star;
        assertThat(underTest).isEqualTo(Token.Star);
    }

    @Test
    public void testDoubleStarToken() {
        Token underTest = Token.DoubleStar;
        assertThat(underTest).isEqualTo(Token.DoubleStar);
    }

    @Test
    public void testNumberSignToken() {
        Token underTest = Token.NumberSign;
        assertThat(underTest).isEqualTo(Token.NumberSign);
    }

    @Test
    public void testDoubleNumberSignToken() {
        Token underTest = Token.DoubleNumberSign;
        assertThat(underTest).isEqualTo(Token.DoubleNumberSign);
    }

    @Test
    public void testGreaterThanSignToken() {
        Token underTest = Token.GreaterThanSign;
        assertThat(underTest).isEqualTo(Token.GreaterThanSign);
    }

    @Test
    public void testTextToken() {
        Token underTest = Token.createTextToken("foo");
        assertThat(underTest.isTextToken()).isTrue();
        assertThat(underTest.getTextValue()).isEqualTo("foo");
    }

    @Test
    public void testEqualsCategoryWithTwoDifferentTokens() {
        Token starToken = Token.Star;
        Token doubleStarToken = Token.DoubleStar;
        Assert.assertNotEquals(starToken, doubleStarToken);
    }

    @Test
    public void testEqualsCategoryWithTwoEqualTokens() {
        Token starToken = Token.Star;
        Token starTokenTwo = Token.Star;
        Assert.assertEquals(starToken, starTokenTwo);
    }

    @Test
    public void testEqualsValueWithTwoEqualValues() {
        Token tokenFoo = Token.createTextToken("foo");
        Token tokenBar = Token.createTextToken("bar");
        Assert.assertNotEquals(tokenFoo, tokenBar);
    }

    @Test
    public void testEqualsValueWithTwoDifferentValues() {
        Token tokenFoo = Token.createTextToken("foo");
        Token tokenFooTwo = Token.createTextToken("foo");
        Assert.assertEquals(tokenFoo, tokenFooTwo);
    }

    @Test
    public void testToString() {
        Token tokenFoo = Token.createTextToken("foo");
        assertThat(tokenFoo.toString()).isEqualTo("Token{value=foo, category=TEXT}");
    }
}

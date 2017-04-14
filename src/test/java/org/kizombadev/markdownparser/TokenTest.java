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

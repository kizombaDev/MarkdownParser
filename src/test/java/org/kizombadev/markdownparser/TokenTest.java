package org.kizombadev.markdownparser;

import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TokenTest {

    @Test
    public void testCategory() {
        Token underTest = Token.create(Token.Category.NEW_LINE);
        assertThat(underTest.getCategory()).isEqualTo(Token.Category.NEW_LINE);
    }

    @Test
    public void testValue() {
        Token underTest = Token.createText("foo");
        assertThat(underTest.getCategory()).isEqualTo(Token.Category.TEXT);
        assertThat(underTest.getValue()).isEqualTo("foo");
    }

    @Test
    public void testEqualsCategoryWithTwoEqualTokens() {
        Token starToken = Token.create(Token.Category.STAR);
        Token doubleStarToken = Token.create(Token.Category.DOUBLE_STAR);
        Assert.assertNotEquals(starToken, doubleStarToken);
    }

    @Test
    public void testEqualsCategoryWithTwoDifferentTokens() {
        Token starToken = Token.create(Token.Category.STAR);
        Token starTokenTwo = Token.create(Token.Category.STAR);
        Assert.assertEquals(starToken, starTokenTwo);
    }

    @Test
    public void testEqualsValueWithTwoEqualValues() {
        Token tokenFoo = Token.createText("foo");
        Token tokenBar = Token.createText("bar");
        Assert.assertNotEquals(tokenFoo, tokenBar);
    }

    @Test
    public void testEqualsValueWithTwoDifferentValues() {
        Token tokenFoo = Token.createText("foo");
        Token tokenFooTwo = Token.createText("foo");
        Assert.assertEquals(tokenFoo, tokenFooTwo);
    }

    @Test
    public void testToString() {
        Token tokenFoo = Token.createText("foo");
        assertThat(tokenFoo.toString()).isEqualTo("Token{value=foo, category=TEXT}");
    }
}

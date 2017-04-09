package org.kizombadev.markdownparser;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TokenizerTest {
    private Tokenizer underTest;
    private String newLine = System.getProperty("line.separator");

    @Before
    public void Init() {
        underTest = Tokenizer.create();
    }

    @Test
    public void TestMethod1() {
        assertThat(underTest.parse("#")).containsOnly(Token.create(Token.Category.NUMBER_SIGN));
    }

    @Test
    public void TestMethod2() {
        assertThat(underTest.parse("##")).containsOnly(Token.create(Token.Category.DOUBLE_NUMBER_SIGN));
    }

    @Test
    public void TestMethod3() {
        assertThat(underTest.parse("# ##")).containsSequence(Token.create(Token.Category.NUMBER_SIGN), Token.create(Token.Category.DOUBLE_NUMBER_SIGN));
    }

    @Test
    public void TestMethod4() {
        assertThat(underTest.parse("*")).containsOnly(Token.create(Token.Category.STAR));
    }

    @Test
    public void TestMethod5() {
        assertThat(underTest.parse("**")).containsOnly(Token.create(Token.Category.DOUPLE_STAR));
    }

    @Test
    public void TestMethod6() {
        assertThat(underTest.parse(newLine)).containsOnly(Token.create(Token.Category.NEW_LINE));
    }

    @Test
    public void TestMethod7() {
        assertThat(underTest.parse(newLine + newLine)).containsSequence(Token.create(Token.Category.NEW_LINE), Token.create(Token.Category.NEW_LINE));
    }

    @Test
    public void TestMethod8() {
        assertThat(underTest.parse(">")).containsOnly(Token.create(Token.Category.GREATER_THAN_SIGN));
    }

    @Test
    public void TestMethod9() {
        assertThat(underTest.parse("42")).containsOnly(Token.create(Token.Category.TEXT, "42"));
    }


}

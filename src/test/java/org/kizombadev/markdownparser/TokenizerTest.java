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
        assertThat(underTest.parse("#")).containsOnly(Token.create(Token.Category.NumberSign));
    }

    @Test
    public void TestMethod2() {
        assertThat(underTest.parse("##")).containsOnly(Token.create(Token.Category.DoubleNumberSign));
    }

    @Test
    public void TestMethod3() {
        assertThat(underTest.parse("# ##")).containsSequence(Token.create(Token.Category.NumberSign), Token.create(Token.Category.DoubleNumberSign));
    }

    @Test
    public void TestMethod4() {
        assertThat(underTest.parse("*")).containsOnly(Token.create(Token.Category.Star));
    }

    @Test
    public void TestMethod5() {
        assertThat(underTest.parse("**")).containsOnly(Token.create(Token.Category.DoupleStar));
    }

    @Test
    public void TestMethod6() {
        assertThat(underTest.parse(newLine)).containsOnly(Token.create(Token.Category.NewLine));
    }

    @Test
    public void TestMethod7() {
        assertThat(underTest.parse(newLine + newLine)).containsSequence(Token.create(Token.Category.NewLine), Token.create(Token.Category.NewLine));
    }

    @Test
    public void TestMethod8() {
        assertThat(underTest.parse(">")).containsOnly(Token.create(Token.Category.GreaterThanSign));
    }

    @Test
    public void TestMethod9() {
        assertThat(underTest.parse("42")).containsOnly(Token.create(Token.Category.Text, "42"));
    }


}

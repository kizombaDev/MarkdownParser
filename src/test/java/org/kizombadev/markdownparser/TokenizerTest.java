package org.kizombadev.markdownparser;


import com.google.common.collect.ImmutableList;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TokenizerTest {
    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();
    private Tokenizer underTest;
    private String newLine = System.lineSeparator();

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
        assertThat(underTest.parse("# ##")).containsSequence(Token.create(Token.Category.NUMBER_SIGN), Token.createText(" "), Token.create(Token.Category.DOUBLE_NUMBER_SIGN));
    }

    @Test
    public void TestMethod4() {
        assertThat(underTest.parse("*")).containsOnly(Token.create(Token.Category.STAR));
    }

    @Test
    public void TestMethod5() {
        assertThat(underTest.parse("**")).containsOnly(Token.create(Token.Category.DOUBLE_STAR));
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
        assertThat(underTest.parse("42")).containsOnly(Token.createText("42"));
    }

    @Test
    public void TestMethod10() {
        assertThat(underTest.parse(" #")).containsSequence(Token.createText(" "), Token.create(Token.Category.NUMBER_SIGN));
    }

    @Test
    public void TestMethod11() {
        assertThat(underTest.parse("# Foo")).containsSequence(Token.create(Token.Category.NUMBER_SIGN), Token.createText(" Foo"));
    }

    @Test
    public void TestMethod12() {
        assertThat(underTest.parse("# *Foo*")).containsSequence(Token.create(Token.Category.NUMBER_SIGN),
                Token.createText(" "),
                Token.create(Token.Category.STAR),
                Token.createText("Foo"),
                Token.create(Token.Category.STAR));
    }

    @Test
    public void TestMethod13() {
        assertThat(underTest.parse("*Foo* **Bar**")).containsSequence(
                Token.create(Token.Category.STAR),
                Token.createText("Foo"),
                Token.create(Token.Category.STAR),
                Token.createText(" "),
                Token.create(Token.Category.DOUBLE_STAR),
                Token.createText("Bar"),
                Token.create(Token.Category.DOUBLE_STAR));
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

        ImmutableList<Token> expectedTokens = ImmutableList.copyOf(new Token[]{Token.create(Token.Category.NUMBER_SIGN),
                Token.createText("Das ist eine h1-Uberschrift"),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.DOUBLE_NUMBER_SIGN),
                Token.createText("Das ist eine h2-Uberschrift"),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.STAR),
                Token.createText("Erster Punkt"),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.STAR),
                Token.createText("Zweiter Punkt"),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.STAR),
                Token.createText("Ein neuer Punkt"),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.DOUBLE_STAR),
                Token.createText("Das hier ist Fett"),
                Token.create(Token.Category.DOUBLE_STAR),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.STAR),
                Token.createText("Das ist ein kursiver Text"),
                Token.create(Token.Category.STAR),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.NEW_LINE),
                Token.create(Token.Category.GREATER_THAN_SIGN),
                Token.createText("Das ist mein Zitat")});

        ImmutableList<Token> actualTokens = underTest.parse(markdown).asList();

        softly.assertThat(actualTokens).hasSize(expectedTokens.size());

        for (int i = 0; i < expectedTokens.size(); i++) {
            softly.assertThat(actualTokens.get(i)).as("Token index is %s", i).isEqualTo(expectedTokens.get(i));
        }

    }
}

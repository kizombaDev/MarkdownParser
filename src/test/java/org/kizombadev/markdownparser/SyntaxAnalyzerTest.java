package org.kizombadev.markdownparser;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.*;

import static org.assertj.core.api.Assertions.assertThat;

public class SyntaxAnalyzerTest {
    private SyntaxAnalyzer underTest;

    @Before
    public void init() {
        underTest = SyntaxAnalyzer.create();
    }

    @Test
    public void testNumberSignText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BigHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void testDoubleNumberSignText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleNumberSign, Token.createTextToken("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(SmallHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void testNumberSignTextNewLineDoubleNumberSingText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo"), Token.NewLine, Token.DoubleNumberSign, Token.createTextToken("Bar")));
        assertThat(syntax.getChildren()).hasSize(2);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BigHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
        assertThat(syntax.getChildren().get(1)).isExactlyInstanceOf(SmallHeadline.class);
        assertThat(syntax.getChildren().get(1).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void testNumberSignTextDoubleStartTextDoubleStar() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.NumberSign, Token.createTextToken("Foo"), Token.DoubleStar, Token.createTextToken("Bar"), Token.DoubleStar));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BigHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(1)).isExactlyInstanceOf(BoldSyntax.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(1).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void testGreaterThanSignText() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.GreaterThanSign, Token.createTextToken("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(Quotation.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void testDoubleStarTextDoubleStar() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.DoubleStar, Token.createTextToken("Foo"), Token.DoubleStar));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BoldSyntax.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void testStarTextStar() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.Star, Token.createTextToken("Foo"), Token.Star));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(ItalicSyntax.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }
}

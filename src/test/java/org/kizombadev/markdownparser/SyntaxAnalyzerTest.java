package org.kizombadev.markdownparser;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Ignore;
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
    public void test() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.create(Token.Category.NUMBER_SIGN), Token.createText("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BigHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void test2() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.create(Token.Category.DOUBLE_NUMBER_SIGN), Token.createText("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(SmallHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void test3() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.create(Token.Category.NUMBER_SIGN), Token.createText("Foo"), Token.create(Token.Category.NEW_LINE), Token.create(Token.Category.DOUBLE_NUMBER_SIGN), Token.createText("Bar")));
        assertThat(syntax.getChildren()).hasSize(2);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BigHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
        assertThat(syntax.getChildren().get(1)).isExactlyInstanceOf(SmallHeadline.class);
        assertThat(syntax.getChildren().get(1).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    public void test5() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.create(Token.Category.GREATER_THAN_SIGN), Token.createText("Foo")));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(Quotation.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

    @Test
    @Ignore
    public void test4() {
        Syntax syntax = underTest.parse(ImmutableList.of(Token.create(Token.Category.NUMBER_SIGN), Token.createText("Foo"), Token.create(Token.Category.DOUBLE_STAR), Token.createText("Bar"), Token.create(Token.Category.DOUBLE_STAR)));
        assertThat(syntax.getChildren()).hasSize(1);
        assertThat(syntax.getChildren().get(0)).isExactlyInstanceOf(BigHeadline.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(1)).isExactlyInstanceOf(BoldSyntax.class);
        assertThat(syntax.getChildren().get(0).getChildren().get(1).getChildren().get(0)).isExactlyInstanceOf(TextSyntax.class);
    }

}

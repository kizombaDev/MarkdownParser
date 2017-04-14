package org.kizombadev.markdownparser;

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.*;
import org.kizombadev.markdownparser.entities.interfaces.MutableSyntax;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlGeneratorTest {
    private HtmlGenerator generator;

    @Before
    public void init() {
        generator = HtmlGenerator.create();
    }

    @Test
    public void test() {
        assertElementWithOneTextChild(new BigHeadline(), "h1");

    }

    @Test
    public void test2() {
        assertElementWithOneTextChild(new SmallHeadline(), "h2");

    }

    @Test
    public void test3() {
        assertElementWithOneTextChild(new ItalicSyntax(), "i");
    }

    @Test
    public void test4() {
        assertElementWithOneTextChild(new BoldSyntax(), "b");
    }

    private void assertElementWithOneTextChild(MutableSyntax syntax, String htmlTag) {
        //arrange
        syntax.addChild(new TextSyntax("Foo"));
        RootSyntax root = new RootSyntax();
        root.addChild(syntax);

        //act
        String html = generator.parse(root);

        //assert
        assertThat(html).isEqualTo("<" + htmlTag + ">Foo</" + htmlTag + ">");
    }
}

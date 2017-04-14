package org.kizombadev.markdownparser;

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.markdownparser.entities.*;
import org.kizombadev.markdownparser.entities.interfaces.MutableSyntax;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlGeneratorTest {
    private static final String HTML_START = "<!DOCTYPE html><html><body>";
    private static final String HTML_END = "</body></html>";
    private HtmlGenerator generator;

    @Before
    public void init() {
        generator = HtmlGenerator.create();
    }

    @Test
    public void testBigHeadline() {
        assertElementWithOneTextChild(new BigHeadline(), "h1");
    }

    @Test
    public void testSmallHeadline() {
        assertElementWithOneTextChild(new SmallHeadline(), "h2");
    }

    @Test
    public void testItalic() {
        assertElementWithOneTextChild(new ItalicSyntax(), "i");
    }

    @Test
    public void testBold() {
        assertElementWithOneTextChild(new BoldSyntax(), "b");
    }

    @Test
    public void testQuotation() {
        assertElementWithOneTextChild(new QuotationSyntax(), "blockquote");
    }

    private void assertElementWithOneTextChild(MutableSyntax syntax, String htmlTag) {
        //arrange
        syntax.addChild(new TextSyntax("Foo"));
        RootSyntax root = new RootSyntax();
        root.addChild(syntax);

        //act
        String html = generator.parse(root);

        //assert
        assertThat(html).isEqualTo(HTML_START + "<" + htmlTag + ">Foo</" + htmlTag + ">" + HTML_END);
    }
}

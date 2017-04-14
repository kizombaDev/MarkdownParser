package org.kizombadev.markdownparser;

import org.kizombadev.markdownparser.entities.*;
import org.kizombadev.markdownparser.entities.interfaces.ImmutableSyntax;

public class HtmlGenerator {

    private ImmutableSyntax root;

    private HtmlGenerator() {
    }

    public static HtmlGenerator create() {
        return new HtmlGenerator();
    }

    public String parse(ImmutableSyntax root) {
        this.root = root;

        StringBuilder html = new StringBuilder();

        handle(root, html);

        return html.toString();
    }

    private void handle(ImmutableSyntax root, StringBuilder html) {
        for (ImmutableSyntax child : root.getChildren()) {

            if (child instanceof BigHeadline) {
                handleTag("h1", child, html);
            } else if (child instanceof SmallHeadline) {
                handleTag("h2", child, html);
            } else if (child instanceof TextSyntax) {
                html.append(child.convertTo(TextSyntax.class).getText());
            } else if (child instanceof BoldSyntax) {
                handleTag("b", child, html);
            } else if (child instanceof ItalicSyntax) {
                handleTag("i", child, html);
            } else if (child instanceof ItalicSyntax) {
                handleTag("b", child, html);
            }
        }
    }

    private void handleTag(String htmlTag, ImmutableSyntax child, StringBuilder html) {
        html.append("<" + htmlTag + ">");
        handle(child, html);
        html.append("</" + htmlTag + ">");
    }
}

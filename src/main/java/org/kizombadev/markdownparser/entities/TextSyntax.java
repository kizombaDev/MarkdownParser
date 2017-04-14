package org.kizombadev.markdownparser.entities;

public class TextSyntax extends Syntax {

    private String text;

    public TextSyntax(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

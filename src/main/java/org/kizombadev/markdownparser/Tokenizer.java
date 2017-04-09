package org.kizombadev.markdownparser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private List<Token> tokens = new ArrayList<>();

    private StringBuilder text = new StringBuilder();

    public static Tokenizer create() {
        return new Tokenizer();
    }

    public List<Token> parse(String input) {
        InputStream tokenStream = InputStream.create(input);

        while (tokenStream.hasNext()) {
            tokenStream.next();

            if (tokenStream.current() == '\r' && tokenStream.hasNext() && tokenStream.showNext() == '\n') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.NEW_LINE));
                tokenStream.next();
            } else if (tokenStream.current() == '#' && tokenStream.hasNext() && tokenStream.showNext() == '#') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.DOUBLE_NUMBER_SIGN));
                tokenStream.next();
            } else if (tokenStream.current() == '#') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.NUMBER_SIGN));
            } else if (tokenStream.current() == '*' && tokenStream.hasNext() && tokenStream.showNext() == '*') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.DOUPLE_STAR));
                tokenStream.next();
            } else if (tokenStream.current() == '*') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.STAR));
            } else if (tokenStream.current() == '>') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.GREATER_THAN_SIGN));
            } else {
                text.append(tokenStream.current());
            }
        }

        handleEndOfText();


        return tokens;
    }

    private void handleEndOfText() {
        if (text.length() == 0) {
            return;
        }

        String currentText = text.toString().trim();

        if (null == currentText || "".equals(currentText)) {
            return;
        }

        tokens.add(Token.create(Token.Category.TEXT, currentText));
        text = new StringBuilder();
    }
}
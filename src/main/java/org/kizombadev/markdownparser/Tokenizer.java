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
                tokens.add(Token.create(Token.Category.NewLine));
                tokenStream.next();
            } else if (tokenStream.current() == '#' && tokenStream.hasNext() && tokenStream.showNext() == '#') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.DoubleNumberSign));
                tokenStream.next();
            } else if (tokenStream.current() == '#') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.NumberSign));
            } else if (tokenStream.current() == '*' && tokenStream.hasNext() && tokenStream.showNext() == '*') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.DoupleStar));
                tokenStream.next();
            } else if (tokenStream.current() == '*') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.Star));
            } else if (tokenStream.current() == '>') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.GreaterThanSign));
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

        tokens.add(Token.create(Token.Category.Text, text.toString().trim()));
        text = new StringBuilder();
    }
}
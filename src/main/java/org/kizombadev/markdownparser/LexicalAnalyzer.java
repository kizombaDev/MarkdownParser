package org.kizombadev.markdownparser;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.kizombadev.markdownparser.entities.Token;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LexicalAnalyzer {
    private List<Token> tokens = new ArrayList<>();

    private StringBuilder text = new StringBuilder();

    public static LexicalAnalyzer create() {
        return new LexicalAnalyzer();
    }

    public ImmutableCollection<Token> parse(String input) {
        checkNotNull(input);

        InputStream tokenStream = InputStream.create(input);

        while (tokenStream.hasNext()) {
            tokenStream.next();

            if (tokenStream.current() == '\r' && tokenStream.hasNext() && tokenStream.showNext() == '\n') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.NEW_LINE));
                tokenStream.next();
            } else if (tokenStream.current() == '\n') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.NEW_LINE));
            } else if (tokenStream.current() == '#' && tokenStream.hasNext() && tokenStream.showNext() == '#') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.DOUBLE_NUMBER_SIGN));
                tokenStream.next();
            } else if (tokenStream.current() == '#') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.NUMBER_SIGN));
            } else if (tokenStream.current() == '*' && tokenStream.hasNext() && tokenStream.showNext() == '*') {
                handleEndOfText();
                tokens.add(Token.create(Token.Category.DOUBLE_STAR));
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

        return ImmutableList.copyOf(tokens);
    }

    private void handleEndOfText() {
        if (text.length() == 0) {
            return;
        }

        String currentText = text.toString();

        if (Strings.isNullOrEmpty(currentText)) {
            return;
        }

        tokens.add(Token.createText(currentText));
        text = new StringBuilder();
    }
}
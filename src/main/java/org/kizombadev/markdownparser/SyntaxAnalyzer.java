package org.kizombadev.markdownparser;

import com.google.common.collect.ImmutableList;
import org.kizombadev.markdownparser.entities.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class SyntaxAnalyzer {

    private ImmutableList<Token> tokens;
    private int tokenIndex = 0;
    private Deque<State> stack = new ArrayDeque<>();

    public static SyntaxAnalyzer create() {
        return new SyntaxAnalyzer();
    }

    public Syntax parse(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        RootSyntax root = new RootSyntax();

        Token currentToken = currentToken();


        while (currentToken != null) {

            if (currentToken.equals(Token.NumberSign)) {
                handleBigHeadlineLine(root);
            } else if (currentToken.equals(Token.DoubleNumberSign)) {
                handleSmallHeadlineLine(root);
            } else if (currentToken.equals(Token.GreaterThanSign)) {
                handleQuotation(root);
            } else if (currentToken.equals(Token.DoubleStar)) {
                handleBold(root);
            } else if (currentToken.equals(Token.Star)) {
                handleItalic(root);
            } else if (currentToken.equals(Token.NewLine)) {
                stepTokenForward();
            }

            currentToken = currentToken();
        }

        return root;
    }

    private void handleQuotation(RootSyntax currentRoot) {
        Syntax quotation = new Quotation();
        stepTokenForward();

        handleLineContainer(quotation);
        currentRoot.addChild(quotation);
    }

    private void handleBigHeadlineLine(Syntax currentRoot) {
        Syntax bigHeadline = new BigHeadline();
        stepTokenForward();

        handleLineContainer(bigHeadline);
        currentRoot.addChild(bigHeadline);
    }

    private void handleSmallHeadlineLine(Syntax currentRoot) {
        Syntax smallHeadline = new SmallHeadline();
        stepTokenForward();


        handleLineContainer(smallHeadline);
        currentRoot.addChild(smallHeadline);
    }

    private void handleLineContainer(Syntax currentRoot) {

        Token currentToken = currentToken();

        while (currentToken != null && !currentToken.equals(Token.NewLine)) {

            if (currentToken.equals(Token.DoubleStar)) {
                if (!stack.isEmpty() && stack.peek().equals(State.BOLD_BEGIN)) {
                    stack.push(State.BOLD_END);
                    return;
                } else {
                    handleBold(currentRoot);
                }
            } else if (currentToken.equals(Token.Star)) {
                if (!stack.isEmpty() && stack.peek().equals(State.ITALIC_BEGIN)) {
                    stack.push(State.ITALIC_END);
                    return;
                } else {
                    handleItalic(currentRoot);
                }
            } else if (currentToken.isTextToken()) {
                currentRoot.addChild(new TextSyntax());
                stepTokenForward();
            }

            currentToken = currentToken();
        }
    }

    private void handleBold(Syntax currentRoot) {
        handleBeginEndToken(State.BOLD_BEGIN, State.BOLD_END, new BoldSyntax(), currentRoot);

    }

    private void handleItalic(Syntax currentRoot) {
        handleBeginEndToken(State.ITALIC_BEGIN, State.ITALIC_END, new ItalicSyntax(), currentRoot);
    }

    private void handleBeginEndToken(State beginSate, State endState, Syntax newSyntaxElement, Syntax currentRoot) {
        stepTokenForward();

        stack.push(beginSate);
        TempContainer tempContainer = new TempContainer();
        handleLineContainer(tempContainer);

        if (stack.peek().equals(endState)) {

            for (Syntax child : tempContainer.getChildren()) {
                newSyntaxElement.addChild(child);
            }
            currentRoot.addChild(newSyntaxElement);

            stack.pop();
            stack.pop();
        }
    }

    private Token currentToken() {
        if (tokenIndex >= tokens.size()) {
            return null;
        }

        return tokens.get(tokenIndex);
    }

    private void stepTokenForward() {
        tokenIndex++;
    }

    private enum State {
        BOLD_BEGIN,
        BOLD_END, ITALIC_BEGIN, ITALIC_END,

    }
}

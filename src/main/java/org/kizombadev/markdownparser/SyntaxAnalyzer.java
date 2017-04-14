package org.kizombadev.markdownparser;

import com.google.common.collect.ImmutableList;
import org.kizombadev.markdownparser.entities.*;

import java.util.Stack;

public class SyntaxAnalyzer {

    private ImmutableList<Token> tokens;
    private int tokenIndex = 0;
    private Stack<State> stack = new Stack<>();

    public static SyntaxAnalyzer create() {
        return new SyntaxAnalyzer();
    }

    public Syntax parse(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        RootSyntax root = new RootSyntax();

        while (currentToken() != null) {
            if (currentToken().equals(Token.NumberSign)) {
                handleBigHeadlineLine(root);
            } else if (currentToken().equals(Token.DoubleNumberSign)) {
                handleSmallHeadlineLine(root);
            } else if (currentToken().equals(Token.GreaterThanSign)) {
                handleQuotation(root);
            } else if (currentToken().equals(Token.DoubleStar)) {
                handleBold(root);
            } else if (currentToken().equals(Token.Star)) {
                handleItalic(root);
            }

            if (currentToken() != null && currentToken().equals(Token.NewLine)) {
                stepTokenForward();
            }
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

        while (currentToken() != null && !currentToken().equals(Token.NewLine)) {

            if (currentToken() != null && currentToken().equals(Token.DoubleStar)) {
                if (!stack.isEmpty() && stack.peek().equals(State.BoldBegin)) {
                    stack.push(State.BoldEnd);
                    return;
                } else {
                    handleBold(currentRoot);
                }
            } else if (currentToken() != null && currentToken().equals(Token.Star)) {
                if (!stack.isEmpty() && stack.peek().equals(State.ItalicBegin)) {
                    stack.push(State.ItalicEnd);
                    return;
                } else {
                    handleItalic(currentRoot);
                }
            } else if (currentToken() != null && currentToken().isTextToken()) {
                currentRoot.addChild(new TextSyntax());
                stepTokenForward();
            }
        }
    }

    private void handleBold(Syntax currentRoot) {
        stepTokenForward();


        stack.push(State.BoldBegin);
        TempContainer tempContainer = new TempContainer();
        handleLineContainer(tempContainer);

        if (stack.peek().equals(State.BoldEnd)) {
            Syntax bold = new BoldSyntax();

            for (Syntax child : tempContainer.getChildren()) {
                bold.addChild(child);
            }
            currentRoot.addChild(bold);

            stack.pop();
            stack.pop();
        }
    }

    private void handleItalic(Syntax currentRoot) {
        stepTokenForward();


        stack.push(State.ItalicBegin);
        TempContainer tempContainer = new TempContainer();
        handleLineContainer(tempContainer);

        if (stack.peek().equals(State.ItalicEnd)) {
            Syntax italic = new ItalicSyntax();

            for (Syntax child : tempContainer.getChildren()) {
                italic.addChild(child);
            }
            currentRoot.addChild(italic);

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

    private Token nextToken() {
        if (tokenIndex + 1 >= tokens.size()) {
            return null;
        }

        return tokens.get(tokenIndex + 1);
    }

    private void stepTokenForward() {
        tokenIndex++;
    }

    private enum State {
        BoldBegin,
        BoldEnd, ItalicBegin, ItalicEnd,

    }
}

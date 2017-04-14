package org.kizombadev.markdownparser;

import com.google.common.collect.ImmutableList;
import org.kizombadev.markdownparser.entities.*;

import java.util.Stack;

public class SyntaxAnalyzer {

    private ImmutableList<Token> tokens;
    private int tokenIndex = 0;
    private Stack<Token.Category> stack = new Stack<>();

    public static SyntaxAnalyzer create() {
        return new SyntaxAnalyzer();
    }

    public Syntax parse(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        RootSyntax root = new RootSyntax();

        while (currentToken() != null) {
            if (currentToken().getCategory().equals(Token.Category.NUMBER_SIGN)) {
                handleBigHeadlineLine(root);
            } else if (currentToken().getCategory().equals(Token.Category.DOUBLE_NUMBER_SIGN)) {
                handleSmallHeadlineLine(root);
            } else if (currentToken().getCategory().equals(Token.Category.GREATER_THAN_SIGN)) {
                handleQuotation(root);
            }

            if (currentToken() != null && currentToken().getCategory().equals(Token.Category.NEW_LINE)) {
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

        while (currentToken() != null && !currentToken().getCategory().equals(Token.Category.NEW_LINE)) {

            if (currentToken() != null && currentToken().getCategory().equals(Token.Category.DOUBLE_STAR)) {
                handleBold(currentRoot);
            } else if (currentToken() != null && currentToken().getCategory().equals(Token.Category.TEXT)) {
                currentRoot.addChild(new TextSyntax());
                stepTokenForward();
            }
        }
    }

    private void handleBold(Syntax currentRoot) {
        stepTokenForward();

        TempContainer tempContainer = null;

        if (!stack.isEmpty() && stack.peek().equals(Token.Category.DOUBLE_STAR)) {
            stack.pop();
            Syntax bold = new BoldSyntax();

            for (Syntax child : tempContainer.getChildren()) {
                bold.addChild(child);
            }
            currentRoot.addChild(bold);
        } else {
            stack.push(Token.Category.DOUBLE_STAR);
            tempContainer = new TempContainer();
            handleLineContainer(tempContainer);
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
}

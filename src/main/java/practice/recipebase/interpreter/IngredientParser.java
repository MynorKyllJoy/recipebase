package practice.recipebase.interpreter;

import practice.recipebase.TokenType;
import practice.recipebase.exceptions.WrongTokenTypeException;

/*
 This is a pratt parser which structures the tokens given by a tokenizer into a tree. There are no pre or suffixes here.
 Operands connecting two numbers have the highest priority, followed by space since we want to connect the words next
 to one another. Then the alternative operands since everything following an alternative operand is considered part of
 the alternative ingredient. Finally, there is the comma, which separates two sentences/expression and has the lowest
 precedence.
 */

public class IngredientParser {
    private final IngredientTokenizer tokenizer;
    private final int minPrecedence;

    public IngredientParser(IngredientTokenizer tokenizer, int minPrecedence) {
        this.tokenizer = tokenizer;
        this.minPrecedence = minPrecedence;
    }

    // TODO: Code too long, try to shorten
    public Expression parse() throws WrongTokenTypeException {
        Token firstToken = this.tokenizer.next();

        // like in a math formula, the expression should not start with an operand
        if(firstToken.type() == TokenType.OPERAND || firstToken.type() == TokenType.ALTERNATIVE) {
            throw new WrongTokenTypeException(
                    firstToken.value() + " is " + firstToken.type() + " and should not come first."
            );
        }
        Expression leftExpr = new TerminalExpression(firstToken); // process top token of tokenizer
        if(firstToken.type() == TokenType.OPEN_BRACKET) {
            // process contents inside brackets and skip closed bracket
            if(tokenizer.peek().type() == TokenType.ALTERNATIVE) {
                //it is possible that an alternative operand follows immediately after an open bracket
                tokenizer.next();
                leftExpr = new AlternativeExpression(new IngredientParser(tokenizer, 0).parse());
            } else {
                leftExpr = new IngredientParser(tokenizer, 0).parse();
            }
            tokenizer.next();
        }

        Token nextToken = this.tokenizer.peek();
        while(nextToken.type() != TokenType.END && nextToken.type() != TokenType.CLOSE_BRACKET) {
            Precedence precedence = this.getPrecedence(nextToken);
            // if right precedence greater than left, go back and combine with the right word before the left one
            if(precedence.left() < this.minPrecedence)
                break;

            // combine left expression
            Token operand = tokenizer.next();
            Expression rightExpr = new IngredientParser(this.tokenizer, precedence.right()).parse();
            if(operand.type() == TokenType.ALTERNATIVE) {
                rightExpr = new AlternativeExpression(rightExpr);
            }
            leftExpr = new OperandExpression(operand, leftExpr, rightExpr);
            nextToken = tokenizer.peek();
        }
        return leftExpr;
    }


    private Precedence getPrecedence(Token token) throws WrongTokenTypeException {
        // returns the precedence of an (alternative) operand
        if(token.type() != TokenType.OPERAND && token.type() != TokenType.ALTERNATIVE) {
            throw new WrongTokenTypeException(token.value() + " is from type " + token.type()
                    + " neither TokenType.OPERAND nor TokenType.ALTERNATIVE. " +
                    "It has no precedence and should not appear at this position");
        }

        String value = token.value();
        if(TypeConstants.isPatternIn(value, new String[]{"-", "/", "x", "."})) {
            return new Precedence(7, 6);
        } else if(value.equals(" ")) {
            return new Precedence(4, 5);
        } else if(token.type() == TokenType.ALTERNATIVE) {
            return new Precedence(2, 3);
        } else if(value.equals(",")) {
            return new Precedence(0, 1);
        } else {
            throw new WrongTokenTypeException(
                    "The token has correct type of " + token.type() + " but the value '"
                    + token.value() + "' should not have this type!"
            );
        }
    }
}

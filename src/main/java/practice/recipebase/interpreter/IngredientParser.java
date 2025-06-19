package practice.recipebase.interpreter;

import practice.recipebase.TokenType;

public class IngredientParser {
    private final IngredientTokenizer tokenizer;
    private final int minPrecedence;


    public IngredientParser(IngredientTokenizer tokenizer, int minPrecedence) {
        this.tokenizer = tokenizer;
        this.minPrecedence = minPrecedence;
    }


    public Expression parse() throws Exception {
        Token firstToken = this.tokenizer.next();

        if(firstToken.type() == TokenType.OPERAND || firstToken.type() == TokenType.ALTERNATIVE) {
            throw new Exception(firstToken.value() + " is " + firstToken.type() + " and should not come first.");
        }
        Expression leftExpr = new TerminalExpression(firstToken);

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
            Precedence precedence = this.get_precedence(nextToken);
            if(precedence.left() < this.minPrecedence)
                break;

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


    private Precedence get_precedence(Token token) throws Exception {
        String errorMessage = token.value() + " is from type " + token.type()
                + " neither TokenType.OPERAND nor TokenType.ALTERNATIVE. " +
                "It has no precedence and should not appear at this position";
        if(token.type() != TokenType.OPERAND && token.type() != TokenType.ALTERNATIVE) {
            throw new Exception(errorMessage);
        }

        String value = token.value();
        if(this.isIn(value, new String[]{"-", "/", "x", "."})) {
            return new Precedence(7, 6);
        } else if(value.equals(" ")) {
            return new Precedence(5, 4);
        } else if(value.equals(",")) {
            return new Precedence(3, 2);
        } else if(this.isIn(value, new String[]{"or", "use", "substitute"})) {
            return new Precedence(0, 1);
        } else {
            throw new Exception(errorMessage);
        }
    }


    private boolean isIn(String value, String[] constantArray) {
        for(String element : constantArray) {
            if(value.equals(element)) {
                return true;
            }
        }
        return false;
    }
}

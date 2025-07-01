package practice.recipebase.interpreter;

import practice.recipebase.TokenType;

public class OperandExpression implements Expression {
    private final Token operand;
    private final Expression leftExpr;
    private final Expression rightExpr;

    public OperandExpression(Token operand, Expression leftExpr, Expression rightExpr) {
        this.operand = operand;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public IngredientRequirements interpret() {
        IngredientRequirements left = leftExpr.interpret();
        IngredientRequirements right = rightExpr.interpret();

        // number handling
        switch (operand.value()) {
            case "." -> {
                // create double number, the amounts before combining should be integers
                String combinedDouble = left.getAmount().intValue() + "." + right.getAmount().intValue();
                left.setValue(new Token(combinedDouble, TokenType.QUANTITY));
                right.setValue(new Token(null, TokenType.QUANTITY));
            }
            case "/" -> {
                // divide, the amounts before combining should be integers
                double combinedDouble = left.getAmount() / right.getAmount();
                left.setValue(new Token(Double.toString(combinedDouble), TokenType.QUANTITY));
                right.setValue(new Token(null, TokenType.QUANTITY));
            }
            case "+" -> {
                // for 1+1/2, "/" has higher precedence, so it won't calculate 1+1 first
                double combinedDouble = left.getAmount() + right.getAmount();
                left.setValue(new Token(Double.toString(combinedDouble), TokenType.QUANTITY));
                right.setValue(new Token(null, TokenType.QUANTITY));
            }
            case "-" -> {  // minus not hyphen, for a range, add minimum and maximum as measurements
                left.setValue(new Token(right.getUnit(), TokenType.UNIT));
            }
        }
        // merge the one without name into the one with name. if both have a name, prioritise the one with amount
        if(right.getName() == null || (right.getAmount() == null && left.getName() != null)) {
            return left.merge(right);
        }
        return right.merge(left);
    }
}

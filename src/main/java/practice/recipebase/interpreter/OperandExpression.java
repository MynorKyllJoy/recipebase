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
        if(operand.value().equals(".") && left.getAmount() != null && right.getAmount() != null) {
            // create float number, the amounts before combining should be integers
            String combinedFloat = left.getAmount().intValue() + "." + right.getAmount().intValue();
            left.setValue(new Token(combinedFloat, TokenType.QUANTITY));
            right.setValue(new Token(null, TokenType.QUANTITY));
        } else if(operand.value().equals("/")) {
            // divide, the amounts before combining should be integers
            float combinedFloat = left.getAmount() / right.getAmount();
            left.setValue(new Token(Float.toString(combinedFloat), TokenType.QUANTITY));
            right.setValue(new Token(null, TokenType.QUANTITY));
        } else if(operand.value().equals("+")) {
            // for 1+1/2, "/" has higher precedence, so it won't calculate 1+1 first
            float combinedFloat = left.getAmount() + right.getAmount();
            left.setValue(new Token(Float.toString(combinedFloat), TokenType.QUANTITY));
            right.setValue(new Token(null, TokenType.QUANTITY));
        } else if (operand.value().equals("-")) {  // minus not hyphen
            // for a range, add minimum and maximum as measurements
            if(left.getUnit() == null) {
                left.setValue(new Token(right.getUnit(), TokenType.UNIT));
            } else {
                right.setValue(new Token(left.getUnit(), TokenType.UNIT));
            }
        }
        if(right.getName() == null) {
            return left.merge(right);
        }
        return right.merge(left);
    }
}

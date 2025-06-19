package practice.recipebase.interpreter;

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
    public String interpret() {
        return leftExpr.interpret() + operand.value() + rightExpr.interpret();
    }
}

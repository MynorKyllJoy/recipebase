package practice.recipebase.interpreter;

public class AlternativeExpression implements Expression {
    private Expression expression;

    public AlternativeExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String interpret() {
        return expression.interpret();
    }
}

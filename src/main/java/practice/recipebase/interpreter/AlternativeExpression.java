package practice.recipebase.interpreter;

public class AlternativeExpression implements Expression {
    private final Expression expression;

    public AlternativeExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public InterpretedIngredient interpret() {
        InterpretedIngredient ingredient = new InterpretedIngredient();
        return ingredient.addAlternativeIngredient(expression.interpret());
    }
}

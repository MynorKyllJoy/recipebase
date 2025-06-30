package practice.recipebase.interpreter;

public class AlternativeExpression implements Expression {
    private final Expression expression;

    public AlternativeExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public IngredientRequirements interpret() {
        IngredientRequirements ingredient = expression.interpret();
        return ingredient.asAlternativeIngredient();
    }
}

package practice.recipebase.interpreter;

public class AlternativeExpression implements Expression {
    private final Expression expression;

    public AlternativeExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public IngredientRequirementMaker interpret() {
        IngredientRequirementMaker ingredient = expression.interpret();
        return ingredient.asAlternativeIngredient();
    }
}

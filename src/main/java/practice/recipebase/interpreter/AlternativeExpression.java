package practice.recipebase.interpreter;

public class AlternativeExpression implements Expression {
    private final Expression expression;

    public AlternativeExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public IngredientRequirements interpret() {
        // the alternative ingredients in the final IngredientRequirements object should not have alternative
        // ingredients themselves
        IngredientRequirements currIngredient = expression.interpret();
        IngredientRequirements altIngredient = new IngredientRequirements();
        altIngredient.addAllAlternativeIngredients(currIngredient.getAlternativeIngredients());
        currIngredient.removeAltIngredients();
        altIngredient.addAlternativeIngredient(currIngredient);
        return altIngredient;
    }
}

package practice.recipebase.interpreter;

public class TerminalExpression implements Expression {
    private final Token token;

    public TerminalExpression(Token token) {
        this.token = token;
    }

    @Override
    public IngredientRequirementMaker interpret() {
        IngredientRequirementMaker ingredient = new IngredientRequirementMaker();
        ingredient.setValue(token);
        return ingredient;
    }
}

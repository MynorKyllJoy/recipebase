package practice.recipebase.interpreter;

public class TerminalExpression implements Expression {
    private final Token token;

    public TerminalExpression(Token token) {
        this.token = token;
    }

    @Override
    public IngredientRequirements interpret() {
        IngredientRequirements ingredient = new IngredientRequirements();
        ingredient.setValue(token);
        return ingredient;
    }
}

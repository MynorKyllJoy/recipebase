package practice.recipebase.interpreter;

public class TerminalExpression implements Expression {
    private final Token token;

    public TerminalExpression(Token token) {
        this.token = token;
    }

    @Override
    public InterpretedIngredient interpret() {
        InterpretedIngredient ingredient = new InterpretedIngredient();
        ingredient.setValue(token);
        return ingredient;
    }
}

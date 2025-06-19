package practice.recipebase.interpreter;

public class TerminalExpression implements Expression {
    private final Token token;

    public TerminalExpression(Token token) {
        this.token = token;
    }

    @Override
    public String interpret() {
        return this.token.value();
    }
}

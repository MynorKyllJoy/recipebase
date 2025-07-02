package practice.recipebase.interpreter;

import org.junit.jupiter.api.Test;
import practice.recipebase.TokenType;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlternateExpressionTest {
    private TerminalExpression createTerminalExpression(String value, TokenType type) {
        return new TerminalExpression(new Token(value, type));
    }

    @Test
    public void testInterpretTerminalExpression() {
        TerminalExpression terminalExpr = this.createTerminalExpression("name", TokenType.OTHER);
        AlternativeExpression altExpression = new AlternativeExpression(terminalExpr);
        IngredientRequirementMaker actual = altExpression.interpret();


        assertNull(actual.getName());
        assertNull(actual.getAmount());
        assertNull(actual.getUnit());
        assertTrue(actual.getStates().isEmpty());
        assertTrue(actual.getAlternativeMeasurements().isEmpty());
        assertEquals(1 , actual.getAlternativeIngredients().size());

        AlternativeIngredient actualAltIngredient = actual.getAlternativeIngredients().getFirst();
        AlternativeIngredient expectedAltIngredient = new AlternativeIngredient(
                "name", null, null, new HashSet<>(), new ArrayList<>()
        );
        assertEquals(expectedAltIngredient, actualAltIngredient);
    }


    @Test
    public void testInterpretOperandExpression() {
        TerminalExpression nameExpr = this.createTerminalExpression("name", TokenType.OTHER);
        TerminalExpression amountExpr = this.createTerminalExpression("1", TokenType.QUANTITY);
        OperandExpression operandExpr = new OperandExpression(
                new Token(" ", TokenType.OPERAND),
                nameExpr,
                amountExpr
        );
        AlternativeExpression altExpression = new AlternativeExpression(operandExpr);
        IngredientRequirementMaker actual = altExpression.interpret();


        assertNull(actual.getName());
        assertNull(actual.getAmount());
        assertNull(actual.getUnit());
        assertTrue(actual.getStates().isEmpty());
        assertTrue(actual.getAlternativeMeasurements().isEmpty());
        assertEquals(1 , actual.getAlternativeIngredients().size());

        AlternativeIngredient actualAltIngredient = actual.getAlternativeIngredients().getFirst();
        AlternativeIngredient expectedAltIngredient = new AlternativeIngredient(
                "name", null, 1d, new HashSet<>(), new ArrayList<>()
        );
        assertEquals(expectedAltIngredient, actualAltIngredient);
    }
}

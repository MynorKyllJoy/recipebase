package practice.recipebase.interpreter;

import org.junit.jupiter.api.Test;
import practice.recipebase.TokenType;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalExpressionTest {
    @Test
    public void testTerminalExpressionOther() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("name", TokenType.OTHER));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertEquals("name", maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testTerminalExpressionAmount() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("1", TokenType.QUANTITY));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertEquals(1d, maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());

    }

    @Test
    public void testTerminalExpressionUnit() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("unit", TokenType.UNIT));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertEquals("unit", maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());

    }

    @Test
    public void testTerminalExpressionState() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("state", TokenType.STATE));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertEquals(1, maker.getStates().size());
        assertTrue(maker.getStates().contains("state"));
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());

    }

    @Test
    public void testTerminalExpressionOperand() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("-", TokenType.OPERAND));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testTerminalExpressionPreposition() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("of", TokenType.PREPOSITION));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testTerminalExpressionOpenBracket() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("(", TokenType.OPEN_BRACKET));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testTerminalExpressionCloseBracket() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token(")", TokenType.CLOSE_BRACKET));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testTerminalExpressionAlternative() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("or", TokenType.ALTERNATIVE));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testTerminalExpressionEnd() {
        TerminalExpression terminalExpression = new TerminalExpression(new Token("", TokenType.END));
        IngredientRequirementMaker maker = terminalExpression.interpret();

        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }
}

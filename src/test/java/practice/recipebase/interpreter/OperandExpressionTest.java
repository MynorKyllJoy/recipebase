package practice.recipebase.interpreter;

import org.junit.jupiter.api.Test;
import practice.recipebase.TokenType;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class OperandExpressionTest {
    private TerminalExpression createTerminalExpression(String value, TokenType type) {
        return new TerminalExpression(new Token(value, type));
    }

    @Test
    public void testInterpretSimple() {
        TerminalExpression amount = this.createTerminalExpression("1", TokenType.QUANTITY);
        TerminalExpression unit = this.createTerminalExpression("unit", TokenType.UNIT);
        OperandExpression operandExpr = new OperandExpression(new Token(" ", TokenType.OPERAND), amount, unit);
        IngredientRequirementMaker expected = operandExpr.interpret();

        assertNull(expected.getName());
        assertEquals(1d, expected.getAmount());
        assertEquals("unit", expected.getUnit());
        assertTrue(expected.getStates().isEmpty());
        assertTrue(expected.getAlternativeMeasurements().isEmpty());
        assertTrue(expected.getAlternativeIngredients().isEmpty());
    }

    @Test
    public void testInterpretNumberHandlingDecimal() {
        TerminalExpression one = this.createTerminalExpression("1", TokenType.QUANTITY);
        TerminalExpression two = this.createTerminalExpression("1", TokenType.QUANTITY);
        OperandExpression operandExpr = new OperandExpression(new Token(".", TokenType.OPERAND), one, two);
        IngredientRequirementMaker expected = operandExpr.interpret();

        assertNull(expected.getName());
        assertEquals(1.1d, expected.getAmount());
        assertNull(expected.getUnit());
        assertTrue(expected.getStates().isEmpty());
        assertTrue(expected.getAlternativeMeasurements().isEmpty());
        assertTrue(expected.getAlternativeIngredients().isEmpty());
    }

    @Test
    public void testInterpretNumberHandlingFraction() {
        TerminalExpression one = this.createTerminalExpression("1", TokenType.QUANTITY);
        TerminalExpression two = this.createTerminalExpression("2", TokenType.QUANTITY);
        OperandExpression operandExpr = new OperandExpression(new Token("/", TokenType.OPERAND), one, two);
        IngredientRequirementMaker expected = operandExpr.interpret();

        assertNull(expected.getName());
        assertEquals(0.5d, expected.getAmount());
        assertNull(expected.getUnit());
        assertTrue(expected.getStates().isEmpty());
        assertTrue(expected.getAlternativeMeasurements().isEmpty());
        assertTrue(expected.getAlternativeIngredients().isEmpty());
    }

    @Test
    public void testInterpretNumberHandlingPlus() {
        TerminalExpression one = this.createTerminalExpression("1", TokenType.QUANTITY);
        TerminalExpression two = this.createTerminalExpression("2", TokenType.QUANTITY);
        OperandExpression operandExpr = new OperandExpression(new Token("+", TokenType.OPERAND), one, two);
        IngredientRequirementMaker expected = operandExpr.interpret();

        assertNull(expected.getName());
        assertEquals(3d, expected.getAmount());
        assertNull(expected.getUnit());
        assertTrue(expected.getStates().isEmpty());
        assertTrue(expected.getAlternativeMeasurements().isEmpty());
        assertTrue(expected.getAlternativeIngredients().isEmpty());
    }

    @Test
    public void testInterpretNumberHandlingMinus() {
        TerminalExpression one = this.createTerminalExpression("1", TokenType.QUANTITY);
        TerminalExpression two = this.createTerminalExpression("2", TokenType.QUANTITY);
        OperandExpression operandExpr = new OperandExpression(new Token("-", TokenType.OPERAND), one, two);
        IngredientRequirementMaker expected = operandExpr.interpret();

        assertNull(expected.getName());
        assertEquals(1, expected.getAmount());
        assertNull(expected.getUnit());
        assertTrue(expected.getStates().isEmpty());
        assertEquals(1, expected.getAlternativeMeasurements().size());
        assertTrue(expected.getAlternativeIngredients().isEmpty());

        Measurement expectedAltMeasurement = expected.getAlternativeMeasurements().getFirst();
        assertEquals(new Measurement(null, 2d), expectedAltMeasurement);
    }

    @Test
    public void testInterpretOperandOperand() {
        TerminalExpression one = this.createTerminalExpression("10", TokenType.QUANTITY);
        TerminalExpression two = this.createTerminalExpression("2", TokenType.QUANTITY);
        TerminalExpression three = this.createTerminalExpression("3", TokenType.QUANTITY);
        TerminalExpression four = this.createTerminalExpression("4", TokenType.QUANTITY);
        OperandExpression numerator = new OperandExpression(new Token("+", TokenType.OPERAND), one, two);
        OperandExpression denominator = new OperandExpression(new Token("+", TokenType.OPERAND), three, four);
        OperandExpression fraction = new OperandExpression(new Token(".", TokenType.OPERAND), numerator, denominator);
        IngredientRequirementMaker expected = fraction.interpret();

        assertNull(expected.getName());
        assertEquals(12.7d, expected.getAmount());
        assertNull(expected.getUnit());
        assertTrue(expected.getStates().isEmpty());
        assertTrue(expected.getAlternativeMeasurements().isEmpty());
        assertTrue(expected.getAlternativeIngredients().isEmpty());
    }



    @Test
    public void testInterpretOperandAlternative() {
        TerminalExpression ingredient = this.createTerminalExpression("sugar", TokenType.OTHER);
        AlternativeExpression altIngredient = new AlternativeExpression(
                this.createTerminalExpression("honey", TokenType.OTHER)
        );
        Token operandToken = new Token(" ", TokenType.OPERAND);

        OperandExpression operandExpression = new OperandExpression(operandToken, ingredient, altIngredient);
        IngredientRequirementMaker actual = operandExpression.interpret();

        assertEquals("sugar", actual.getName());
        assertNull(actual.getAmount());
        assertNull(actual.getUnit());
        assertTrue(actual.getStates().isEmpty());
        assertTrue(actual.getAlternativeMeasurements().isEmpty());
        assertEquals(1 , actual.getAlternativeIngredients().size());

        AlternativeIngredient actualAltIngredient = actual.getAlternativeIngredients().getFirst();
        AlternativeIngredient expectedAltIngredient = new AlternativeIngredient(
                "honey", null, null, new HashSet<>(), new ArrayList<>()
        );
        assertEquals(expectedAltIngredient, actualAltIngredient);
    }
}

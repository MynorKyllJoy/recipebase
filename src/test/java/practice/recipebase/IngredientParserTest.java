package practice.recipebase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.interpreter.*;

public class IngredientParserTest {
    @Test
    public void testParseSimple() throws WrongTokenTypeException {
        String ingredientString = "3 diced potato";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("potato");
        assertThat(ingredient.getStates().contains("diced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(3);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseCommaOperand() throws WrongTokenTypeException {
        String ingredientString = "3 medium onion, chopped";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().contains("chopped")).isTrue();
        assertThat(ingredient.getStates().contains("medium")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(3);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseDifferentOrder() throws WrongTokenTypeException {
        String ingredientString = "ginger, grated, 1\" knob";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("ginger");
        assertThat(ingredient.getStates().contains("grated")).isTrue();
        assertThat(ingredient.getStates().contains("knob")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);
        assertThat(ingredient.getUnit()).isEqualTo("\"");
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseBindingWord() throws WrongTokenTypeException {
        String ingredientString = "100g pork sausage (chopped into small pieces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("pork sausage");
        assertThat(ingredient.getStates().contains("chopped into small pieces")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(100);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseBrackets() throws WrongTokenTypeException {
        String ingredientString = "1 medium onion (140g)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        Measurement altMeasurement = ingredient.getAlternativeMeasurements().getFirst();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().contains("medium")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().size()).isEqualTo(1);

        assertThat(altMeasurement.unit()).isEqualTo("g");
        assertThat(altMeasurement.amount()).isEqualTo(140);
    }

    @Test
    public void testToListBracketsInMiddle() throws WrongTokenTypeException {
        String ingredientString = "1 (chopped) onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().contains("chopped")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseAlternativeIngredient() throws WrongTokenTypeException {
        String ingredientString = "1 medium red onion (140g) or white onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        IngredientRequirements alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();
        Measurement altMeasurement = ingredient.getAlternativeMeasurements().getFirst();

        assertThat(ingredient.getName()).isEqualTo("red onion");
        assertThat(ingredient.getStates().contains("medium")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().size()).isEqualTo(1);
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(altMeasurement.unit()).isEqualTo("g");
        assertThat(altMeasurement.amount()).isEqualTo(140);

        assertThat(alternativeIngredient.getName()).isEqualTo("white onion");
        assertThat(alternativeIngredient.getUnit()).isEqualTo(null);
        assertThat(alternativeIngredient.getAmount()).isEqualTo(null);
        assertThat(alternativeIngredient.getStates().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParsePrepositionCombinePrev() throws WrongTokenTypeException {
        String ingredientString = "1 tbsp honey (substitute with 1.5 tbsp sugar)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        IngredientRequirements alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("honey");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("tbsp");
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.getName()).isEqualTo("sugar");
        assertThat(alternativeIngredient.getStates().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getUnit()).isEqualTo("tbsp");
        assertThat(alternativeIngredient.getAmount()).isEqualTo(1.5f);
        assertThat(alternativeIngredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParsePrepositionCombinePrevAndNext() throws WrongTokenTypeException {
        String ingredientString = "200g pork belly with skin scored crosswise into 1 inch cubes";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("pork belly with skin");
        assertThat(ingredient.getStates().contains("scored crosswise into 1 inch cubes")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(200);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseIgnorePreposition() throws WrongTokenTypeException {
        String ingredientString = "1/2 of an onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("an");
        assertThat(ingredient.getAmount()).isEqualTo(0.5f);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseAlternativeWithoutBrackets() throws WrongTokenTypeException {
        String ingredientString = "500g apple or pear";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        IngredientRequirements alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(500);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.getName()).isEqualTo("pear");
        assertThat(alternativeIngredient.getStates().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getUnit()).isEqualTo(null);
        assertThat(alternativeIngredient.getAmount()).isEqualTo(null);
        assertThat(alternativeIngredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseComplexAdditionalInfo() throws WrongTokenTypeException {
        String ingredientString = "400g peeled apple (14.1 ounces) or 500g peeled pear (17.6 ounces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        IngredientRequirements alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().contains("peeled")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(400);
        assertThat(ingredient.getAlternativeMeasurements().size()).isEqualTo(1);
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.getName()).isEqualTo("pear");
        assertThat(alternativeIngredient.getStates().contains("peeled")).isTrue();
        assertThat(alternativeIngredient.getStates().size()).isEqualTo(1);
        assertThat(alternativeIngredient.getUnit()).isEqualTo("g");
        assertThat(alternativeIngredient.getAmount()).isEqualTo(500);
        assertThat(alternativeIngredient.getAlternativeMeasurements().size()).isEqualTo(1);
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseSeriesOfAlternatives() throws WrongTokenTypeException {
        String ingredientString = "3.5 kg minced lamb or beef or chicken";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        IngredientRequirements beefIngredient = ingredient.getAlternativeIngredients().get(0);
        IngredientRequirements chickenIngredient = ingredient.getAlternativeIngredients().get(1);

        assertThat(ingredient.getName()).isEqualTo("lamb");
        assertThat(ingredient.getStates().contains("minced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("kg");
        assertThat(ingredient.getAmount()).isEqualTo(3.5f);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(2);

        assertThat(beefIngredient.getName()).isEqualTo("beef");
        assertThat(beefIngredient.getStates().isEmpty()).isTrue();
        assertThat(beefIngredient.getUnit()).isEqualTo(null);
        assertThat(beefIngredient.getAmount()).isEqualTo(null);
        assertThat(beefIngredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(beefIngredient.getAlternativeIngredients().isEmpty()).isTrue();

        assertThat(chickenIngredient.getName()).isEqualTo("chicken");
        assertThat(chickenIngredient.getStates().isEmpty()).isTrue();
        assertThat(chickenIngredient.getUnit()).isEqualTo(null);
        assertThat(chickenIngredient.getAmount()).isEqualTo(null);
        assertThat(chickenIngredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(chickenIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseMultipleStates() throws WrongTokenTypeException {
        String ingredientString = "1 peeled apple, finely diced";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().contains("peeled")).isTrue();
        assertThat(ingredient.getStates().contains("finely diced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }


    @Test
    public void testParseMultipleMeasurements() throws WrongTokenTypeException {
        String ingredientString = "1 apple (200g, 7oz)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        Measurement altMeasurementGrams = ingredient.getAlternativeMeasurements().get(0);
        Measurement altMeasurementOunces = ingredient.getAlternativeMeasurements().get(1);

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().size()).isEqualTo(2);
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();

        assertThat(altMeasurementGrams.amount()).isEqualTo(200);
        assertThat(altMeasurementGrams.unit()).isEqualTo("g");

        assertThat(altMeasurementOunces.amount()).isEqualTo(7);
        assertThat(altMeasurementOunces.unit()).isEqualTo("oz");
    }


    @Test
    public void testParseMeasurementRange() throws WrongTokenTypeException {
        String ingredientString = "1-3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();
        Measurement altMeasurement = ingredient.getAlternativeMeasurements().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apples");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().size()).isEqualTo(1);
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();

        assertThat(altMeasurement.amount()).isEqualTo(3);
        assertThat(altMeasurement.unit()).isEqualTo(null);
    }


    @Test
    public void testParseHybridFraction() throws WrongTokenTypeException {
        String ingredientString = "1+1/3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apples");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1+1f/3);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }


    @Test
    public void testParseUglyFraction() throws WrongTokenTypeException {
        String ingredientString = "1.1+1.5/3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirements ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apples");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1.1f+1.5f/3);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }


    @Test
    public void testParseIncorrectTokenTypeOrderOperandException() {
        String ingredientString = ", fail";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        IngredientParser parser = new IngredientParser(tokenizer, 0);

        WrongTokenTypeException exception = assertThrows(
                WrongTokenTypeException.class, parser::parse
        );

        String actualErrorMessage = exception.getMessage();
        String expectedErrorMessage = ", is OPERAND and should not come first.";

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testParseIncorrectTokenTypeOrderAlternativeException() {
        String ingredientString = "or fail";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        IngredientParser parser = new IngredientParser(tokenizer, 0);

        WrongTokenTypeException exception = assertThrows(
                WrongTokenTypeException.class, parser::parse
        );

        String actualErrorMessage = exception.getMessage();
        String expectedErrorMessage = "or is ALTERNATIVE and should not come first.";

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testGetPrecedenceIncorrectTokenTypeException() {
        String ingredientString = "fail,,";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        IngredientParser parser = new IngredientParser(tokenizer, 0);

        WrongTokenTypeException exception = assertThrows(
                WrongTokenTypeException.class, parser::parse
        );

        String actualErrorMessage = exception.getMessage();
        String expectedErrorMessage = "The token has correct type of OPERAND but the value ', ,' should not have this type!";

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}

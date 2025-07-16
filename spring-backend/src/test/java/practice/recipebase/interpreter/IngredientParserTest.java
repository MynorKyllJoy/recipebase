package practice.recipebase.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import practice.recipebase.exceptions.WrongTokenTypeException;

public class IngredientParserTest {
    @Test
    public void testParseSimple() throws WrongTokenTypeException {
        String ingredientString = "3 diced potato";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

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
        IngredientRequirementMaker ingredient = parser.interpret();

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
        IngredientRequirementMaker ingredient = parser.interpret();

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
        IngredientRequirementMaker ingredient = parser.interpret();

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
        IngredientRequirementMaker ingredient = parser.interpret();
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
        IngredientRequirementMaker ingredient = parser.interpret();

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
        IngredientRequirementMaker ingredient = parser.interpret();
        AlternativeIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();
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

        assertThat(alternativeIngredient.name()).isEqualTo("white onion");
        assertThat(alternativeIngredient.unit()).isEqualTo(null);
        assertThat(alternativeIngredient.amount()).isEqualTo(null);
        assertThat(alternativeIngredient.states().isEmpty()).isTrue();
        assertThat(alternativeIngredient.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParsePrepositionCombinePrev() throws WrongTokenTypeException {
        String ingredientString = "1 tbsp honey (substitute with 1.5 tbsp sugar)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();
        AlternativeIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("honey");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("tbsp");
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.name()).isEqualTo("sugar");
        assertThat(alternativeIngredient.states().isEmpty()).isTrue();
        assertThat(alternativeIngredient.unit()).isEqualTo("tbsp");
        assertThat(alternativeIngredient.amount()).isEqualTo(1.5f);
        assertThat(alternativeIngredient.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParsePrepositionCombinePrevAndNext() throws WrongTokenTypeException {
        String ingredientString = "200g pork belly with skin scored crosswise into 1 inch cubes";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("pork belly with skin");
        assertThat(ingredient.getStates().contains("scored crosswise into 1 inch cubes")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(200);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseIgnorePrepositionPrefixAmount() throws WrongTokenTypeException {
        String ingredientString = "1/2 of an onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("an");
        assertThat(ingredient.getAmount()).isEqualTo(0.5f);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseIgnorePrepositionPrefixUnit() throws WrongTokenTypeException {
        String ingredientString = "1/2 tsp of sugar";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("sugar");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("tsp");
        assertThat(ingredient.getAmount()).isEqualTo(0.5f);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseAlternativeWithoutBrackets() throws WrongTokenTypeException {
        String ingredientString = "500g apple or pear";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();
        AlternativeIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(500);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.name()).isEqualTo("pear");
        assertThat(alternativeIngredient.states().isEmpty()).isTrue();
        assertThat(alternativeIngredient.unit()).isEqualTo(null);
        assertThat(alternativeIngredient.amount()).isEqualTo(null);
        assertThat(alternativeIngredient.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseComplexAdditionalInfo() throws WrongTokenTypeException {
        String ingredientString = "400g peeled apple (14.1 ounces) or 500g peeled pear (17.6 ounces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();
        AlternativeIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().contains("peeled")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(400);
        assertThat(ingredient.getAlternativeMeasurements().size()).isEqualTo(1);
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.name()).isEqualTo("pear");
        assertThat(alternativeIngredient.states().contains("peeled")).isTrue();
        assertThat(alternativeIngredient.states().size()).isEqualTo(1);
        assertThat(alternativeIngredient.unit()).isEqualTo("g");
        assertThat(alternativeIngredient.amount()).isEqualTo(500);
        assertThat(alternativeIngredient.alternativeMeasurements().size()).isEqualTo(1);
    }

    @Test
    public void testParseSeriesOfAlternatives() throws WrongTokenTypeException {
        String ingredientString = "3.5 kg minced lamb or beef or chicken";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();
        AlternativeIngredient beefIngredient = ingredient.getAlternativeIngredients().get(0);
        AlternativeIngredient chickenIngredient = ingredient.getAlternativeIngredients().get(1);

        assertThat(ingredient.getName()).isEqualTo("lamb");
        assertThat(ingredient.getStates().contains("minced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("kg");
        assertThat(ingredient.getAmount()).isEqualTo(3.5f);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(2);

        assertThat(beefIngredient.name()).isEqualTo("beef");
        assertThat(beefIngredient.states().isEmpty()).isTrue();
        assertThat(beefIngredient.unit()).isEqualTo(null);
        assertThat(beefIngredient.amount()).isEqualTo(null);
        assertThat(beefIngredient.alternativeMeasurements().isEmpty()).isTrue();

        assertThat(chickenIngredient.name()).isEqualTo("chicken");
        assertThat(chickenIngredient.states().isEmpty()).isTrue();
        assertThat(chickenIngredient.unit()).isEqualTo(null);
        assertThat(chickenIngredient.amount()).isEqualTo(null);
        assertThat(chickenIngredient.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseMultipleStates() throws WrongTokenTypeException {
        String ingredientString = "1 peeled apple, finely diced";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

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
        IngredientRequirementMaker ingredient = parser.interpret();
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
        IngredientRequirementMaker ingredient = parser.interpret();
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
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apples");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1+1d/3);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }


    @Test
    public void testParseUglyFraction() throws WrongTokenTypeException {
        String ingredientString = "1.1+1.5/3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apples");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1.1d+1.5d/3);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }


    @Test
    public void testParseOrBetweenStates() throws WrongTokenTypeException {
        String ingredientString = "1 chopped or diced apple";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().contains("chopped")).isTrue();
        assertThat(ingredient.getStates().contains("diced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseOrBetweenStatesComma() throws WrongTokenTypeException {
        String ingredientString = "1 apple, chopped or diced";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().contains("chopped")).isTrue();
        assertThat(ingredient.getStates().contains("diced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseOrInBrackets() throws WrongTokenTypeException {
        String ingredientString = "1 apple (or pear)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();
        AlternativeIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.name()).isEqualTo("pear");
        assertThat(alternativeIngredient.states().isEmpty()).isTrue();
        assertThat(alternativeIngredient.unit()).isEqualTo(null);
        assertThat(alternativeIngredient.amount()).isEqualTo(null);
        assertThat(alternativeIngredient.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseDoubleOrInBrackets() throws WrongTokenTypeException {
        String ingredientString = "1 apple (or pear or pineapple)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(2);

        AlternativeIngredient pear = ingredient.getAlternativeIngredients().getFirst();
        assertThat(pear.name()).isEqualTo("pineapple");
        assertThat(pear.states().isEmpty()).isTrue();
        assertThat(pear.unit()).isEqualTo(null);
        assertThat(pear.amount()).isEqualTo(null);
        assertThat(pear.alternativeMeasurements().isEmpty()).isTrue();

        AlternativeIngredient pineapple = ingredient.getAlternativeIngredients().get(1);
        assertThat(pineapple.name()).isEqualTo("pear");
        assertThat(pineapple.states().isEmpty()).isTrue();
        assertThat(pineapple.unit()).isEqualTo(null);
        assertThat(pineapple.amount()).isEqualTo(null);
        assertThat(pineapple.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseAlternativeIngredientWithoutToken() throws WrongTokenTypeException {
        String ingredientString = "1 bread bun (kaiser bun)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("bread bun");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAlternativeMeasurements().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        AlternativeIngredient alternative = ingredient.getAlternativeIngredients().getFirst();
        assertThat(alternative.name()).isEqualTo("kaiser bun");
        assertThat(alternative.states().isEmpty()).isTrue();
        assertThat(alternative.unit()).isEqualTo(null);
        assertThat(alternative.amount()).isEqualTo(null);
        assertThat(alternative.alternativeMeasurements().isEmpty()).isTrue();
    }

    @Test
    public void testParseNoConcreteAmount() throws WrongTokenTypeException {
        String ingredientString = "some salt";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        IngredientRequirementMaker ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("salt");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("some");
        assertThat(ingredient.getAmount()).isEqualTo(null);
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

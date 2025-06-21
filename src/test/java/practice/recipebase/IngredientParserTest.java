package practice.recipebase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import practice.recipebase.interpreter.Expression;
import practice.recipebase.interpreter.IngredientParser;
import practice.recipebase.interpreter.IngredientTokenizer;
import practice.recipebase.interpreter.InterpretedIngredient;

public class IngredientParserTest {
    @Test
    public void testParseSimple() throws Exception {
        String ingredientString = "3 diced potato";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("potato");
        assertThat(ingredient.getStates().contains("diced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(3);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
    }

    @Test
    public void testParseCommaOperand() throws Exception {
        String ingredientString = "3 medium onion, chopped";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().contains("chopped")).isTrue();
        assertThat(ingredient.getStates().contains("medium")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);;
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(3);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
    }

    @Test
    public void testParseDifferentOrder() throws Exception {
        String ingredientString = "ginger, grated, 1\" knob";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("ginger");
        assertThat(ingredient.getStates().contains("grated")).isTrue();
        assertThat(ingredient.getStates().contains("knob")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(2);
        assertThat(ingredient.getUnit()).isEqualTo("\"");
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
    }

    @Test
    public void testParseBindingWord() throws Exception {
        String ingredientString = "100g pork sausage (chopped into small pieces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("pork sausage");
        assertThat(ingredient.getStates().contains("chopped into small pieces")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(100);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
    }

    @Test
    public void testParseBrackets() throws Exception {
        String ingredientString = "1 medium onion (140g)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();
        InterpretedIngredient additionalInfo = ingredient.getAdditionalInfo().getFirst();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().contains("medium")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAdditionalInfo().size()).isEqualTo(1);

        assertThat(additionalInfo.getName()).isEqualTo(null);
        assertThat(additionalInfo.getUnit()).isEqualTo("g");
        assertThat(additionalInfo.getAmount()).isEqualTo(140);
        assertThat(additionalInfo.getStates().isEmpty()).isTrue();
    }

    @Test
    public void testToListBracketsInMiddle() throws Exception {
        String ingredientString = "1 (chopped) onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().contains("chopped")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
    }

    @Test
    public void testParseAlternativeIngredient() throws Exception {
        String ingredientString = "1 medium red onion (140g) or white onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();
        InterpretedIngredient additionalInfo = ingredient.getAdditionalInfo().getFirst();
        InterpretedIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("red onion");
        assertThat(ingredient.getStates().contains("medium")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo(null);
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAdditionalInfo().size()).isEqualTo(1);
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(additionalInfo.getName()).isEqualTo(null);
        assertThat(additionalInfo.getUnit()).isEqualTo("g");
        assertThat(additionalInfo.getAmount()).isEqualTo(140);
        assertThat(additionalInfo.getStates().isEmpty()).isTrue();

        assertThat(alternativeIngredient.getName()).isEqualTo("white onion");
        assertThat(alternativeIngredient.getUnit()).isEqualTo(null);
        assertThat(alternativeIngredient.getAmount()).isEqualTo(null);
        assertThat(alternativeIngredient.getStates().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParsePrepositionCombinePrev() throws Exception {
        String ingredientString = "1 tbsp honey (substitute with 1.5 tbsp sugar)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();
        InterpretedIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("honey");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("tbsp");
        assertThat(ingredient.getAmount()).isEqualTo(1);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.getName()).isEqualTo("sugar");
        assertThat(alternativeIngredient.getStates().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getUnit()).isEqualTo("tbsp");
        assertThat(alternativeIngredient.getAmount()).isEqualTo(1.5f);
        assertThat(alternativeIngredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParsePrepositionCombinePrevAndNext() throws Exception {
        String ingredientString = "200g pork belly with skin scored crosswise into 1 inch cubes";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("pork belly with skin");
        assertThat(ingredient.getStates().contains("scored crosswise into 1 inch cubes")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(200);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseIgnorePreposition() throws Exception {
        String ingredientString = "1/2 of an onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();

        assertThat(ingredient.getName()).isEqualTo("onion");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("an");
        assertThat(ingredient.getAmount()).isEqualTo(0.5f);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseAlternativeWithoutBrackets() throws Exception {
        String ingredientString = "500g apple or pear";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();
        InterpretedIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().isEmpty()).isTrue();
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(500);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.getName()).isEqualTo("pear");
        assertThat(alternativeIngredient.getStates().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getUnit()).isEqualTo(null);
        assertThat(alternativeIngredient.getAmount()).isEqualTo(null);
        assertThat(alternativeIngredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseComplexAdditionalInfo() throws Exception {
        String ingredientString = "400g peeled apple (14.1 ounces) or 500g peeled pear (17.6 ounces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();
        InterpretedIngredient alternativeIngredient = ingredient.getAlternativeIngredients().getFirst();

        assertThat(ingredient.getName()).isEqualTo("apple");
        assertThat(ingredient.getStates().contains("peeled")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("g");
        assertThat(ingredient.getAmount()).isEqualTo(400);
        assertThat(ingredient.getAdditionalInfo().size()).isEqualTo(1);
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(1);

        assertThat(alternativeIngredient.getName()).isEqualTo("pear");
        assertThat(alternativeIngredient.getStates().contains("peeled")).isTrue();
        assertThat(alternativeIngredient.getStates().size()).isEqualTo(1);
        assertThat(alternativeIngredient.getUnit()).isEqualTo("g");
        assertThat(alternativeIngredient.getAmount()).isEqualTo(500);
        assertThat(alternativeIngredient.getAdditionalInfo().size()).isEqualTo(1);
        assertThat(alternativeIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }

    @Test
    public void testParseSeriesOfAlternatives() throws Exception {
        String ingredientString = "3.5 kg minced lamb or beef or chicken";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        InterpretedIngredient ingredient = parser.interpret();
        InterpretedIngredient beefIngredient = ingredient.getAlternativeIngredients().get(0);
        InterpretedIngredient chickenIngredient = ingredient.getAlternativeIngredients().get(1);

        assertThat(ingredient.getName()).isEqualTo("lamb");
        assertThat(ingredient.getStates().contains("minced")).isTrue();
        assertThat(ingredient.getStates().size()).isEqualTo(1);
        assertThat(ingredient.getUnit()).isEqualTo("kg");
        assertThat(ingredient.getAmount()).isEqualTo(3.5f);
        assertThat(ingredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(ingredient.getAlternativeIngredients().size()).isEqualTo(2);

        assertThat(beefIngredient.getName()).isEqualTo("beef");
        assertThat(beefIngredient.getStates().isEmpty()).isTrue();
        assertThat(beefIngredient.getUnit()).isEqualTo(null);
        assertThat(beefIngredient.getAmount()).isEqualTo(null);
        assertThat(beefIngredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(beefIngredient.getAlternativeIngredients().isEmpty()).isTrue();

        assertThat(chickenIngredient.getName()).isEqualTo("chicken");
        assertThat(chickenIngredient.getStates().isEmpty()).isTrue();
        assertThat(chickenIngredient.getUnit()).isEqualTo(null);
        assertThat(chickenIngredient.getAmount()).isEqualTo(null);
        assertThat(chickenIngredient.getAdditionalInfo().isEmpty()).isTrue();
        assertThat(chickenIngredient.getAlternativeIngredients().isEmpty()).isTrue();
    }
}

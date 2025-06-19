package practice.recipebase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import practice.recipebase.interpreter.Expression;
import practice.recipebase.interpreter.IngredientParser;
import practice.recipebase.interpreter.IngredientTokenizer;
import practice.recipebase.interpreter.Token;

import java.util.List;

public class IngredientParserTest {
    @Test
    public void testParseSimple() throws Exception {
        String ingredientString = "3 diced potato";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("3 diced potato");
    }

    @Test
    public void testParseCommaOperand() throws Exception {
        String ingredientString = "3 medium onion, chopped";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("3 medium onion,chopped");
    }

    @Test
    public void testParseDifferentOrder() throws Exception {
        String ingredientString = "ginger, grated, 1\" knob";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("ginger,grated,1 \" knob");
    }

    @Test
    public void testParseBindingWord() throws Exception {
        String ingredientString = "100g pork sausage (chopped into small pieces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("100 g pork sausage (chopped into small pieces)");
    }

    @Test
    public void testParseBrackets() throws Exception {
        String ingredientString = "1 medium onion (140g)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("1 medium onion (140 g)");
    }

    @Test
    public void testToListBracketsInMiddle() throws Exception {
        String ingredientString = "1 (chopped) onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("1 (chopped) onion");
    }

    @Test
    public void testParseAlternativeIngredient() throws Exception {
        String ingredientString = "1 medium red onion (140g) or white onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("1 medium red onion (140 g)orwhite onion");
    }

    @Test
    public void testParsePrepositionCombinePrev() throws Exception {
        String ingredientString = "1 tbsp honey (substitute with 1.5 tbsp sugar)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("1 tbsp honey (substitute with1.5 tbsp sugar)");
    }

    @Test
    public void testParsePrepositionCombinePrevAndNext() throws Exception {
        String ingredientString = "200g pork belly with skin scored crosswise into 1 inch cubes";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret())
                .isEqualTo("200 g pork belly with skin scored crosswise into 1 inch cubes");
    }

    @Test
    public void testParseIgnorePreposition() throws Exception {
        String ingredientString = "1/2 of an onion";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        Expression parser = new IngredientParser(tokenizer, 0).parse();
        assertThat(parser.interpret()).isEqualTo("1/2 an onion");
    }
}

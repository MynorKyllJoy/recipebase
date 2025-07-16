package practice.recipebase.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import practice.recipebase.TokenType;
import practice.recipebase.exceptions.WrongTokenTypeException;

import java.util.List;

public class IngredientTokenizerTest {
    @Test
    public void testSize() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("3 medium onion, chopped");
        assertThat(tokenizer.size()).isEqualTo(7);
    }


    @Test
    public void testNext() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 carrot");
        assertThat(tokenizer.size()).isEqualTo(3);

        Token quantityToken = tokenizer.next();
        assertThat(quantityToken).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokenizer.size()).isEqualTo(2);

        Token operandToken = tokenizer.next();
        assertThat(operandToken).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokenizer.size()).isEqualTo(1);

        Token ingredientToken = tokenizer.next();
        assertThat(ingredientToken).isEqualTo(new Token("carrot", TokenType.OTHER));
        assertThat(tokenizer.size()).isEqualTo(0);

        Token endToken = tokenizer.next();
        assertThat(endToken).isEqualTo(new Token("", TokenType.END));
        assertThat(tokenizer.size()).isEqualTo(0);
    }


    @Test
    public void testPeek() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 carrot");

        Token quantityToken = tokenizer.peek();
        assertThat(quantityToken).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokenizer.size()).isEqualTo(3);
        Token quantityTokenUnchanged = tokenizer.peek();
        assertThat(quantityTokenUnchanged).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokenizer.size()).isEqualTo(3);
        assertThat(quantityToken).isEqualTo(tokenizer.next());

        Token operandToken = tokenizer.peek();
        assertThat(operandToken).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokenizer.size()).isEqualTo(2);
        assertThat(operandToken).isEqualTo(tokenizer.next());

        Token ingredientToken = tokenizer.peek();
        assertThat(ingredientToken).isEqualTo(new Token("carrot", TokenType.OTHER));
        assertThat(tokenizer.size()).isEqualTo(1);
        assertThat(ingredientToken).isEqualTo(tokenizer.next());

        Token endToken = tokenizer.peek();
        assertThat(endToken).isEqualTo(new Token("", TokenType.END));
        assertThat(tokenizer.size()).isEqualTo(0);
        assertThat(endToken).isEqualTo(tokenizer.next());
    }

    @Test
    public void testTokenStackSimple() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("3 diced potato");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("potato", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("diced", TokenType.STATE));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("3", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(5);
    }

    @Test
    public void testTokenStackCommaOperand() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("3 medium onion, chopped");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("chopped", TokenType.STATE));
        assertThat(tokens.get(1)).isEqualTo(new Token(",", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("onion", TokenType.OTHER));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("medium", TokenType.STATE));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("3", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackDifferentOrder() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("ginger, grated, 1\" knob");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("knob", TokenType.STATE));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("\"", TokenType.UNIT));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.get(5)).isEqualTo(new Token(",", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("grated", TokenType.STATE));
        assertThat(tokens.get(7)).isEqualTo(new Token(",", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("ginger", TokenType.OTHER));
        assertThat(tokens.size()).isEqualTo(9);
    }

    @Test
    public void testTokenStackBindingWord() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("100g pork sausage (chopped into small pieces)");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("chopped into small pieces", TokenType.STATE));
        assertThat(tokens.get(2)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("pork sausage", TokenType.OTHER));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(7)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("100", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(9);
    }

    @Test
    public void testTokenStackBrackets() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 medium onion (140g)");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(2)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(3)).isEqualTo(new Token("140", TokenType.QUANTITY));
        assertThat(tokens.get(4)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("onion", TokenType.OTHER));
        assertThat(tokens.get(7)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("medium", TokenType.STATE));
        assertThat(tokens.get(9)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(10)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(11);
    }

    @Test
    public void testTokenStackBracketsInMiddle() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 (chopped) onion");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("onion", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(3)).isEqualTo(new Token("chopped", TokenType.STATE));
        assertThat(tokens.get(4)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackAlternativeIngredient() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 medium red onion (140g) or white onion");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("white onion", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token("or", TokenType.ALTERNATIVE));
        assertThat(tokens.get(2)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(3)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(4)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(5)).isEqualTo(new Token("140", TokenType.QUANTITY));
        assertThat(tokens.get(6)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(7)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("red onion", TokenType.OTHER));
        assertThat(tokens.get(9)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(10)).isEqualTo(new Token("medium", TokenType.STATE));
        assertThat(tokens.get(11)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(12)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(13);
    }

    @Test
    public void testTokenStackPrepositionCombinePrev() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 tbsp honey (substitute with 1.5 tbsp sugar)");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("sugar", TokenType.OTHER));
        assertThat(tokens.get(2)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(3)).isEqualTo(new Token("tbsp", TokenType.UNIT));
        assertThat(tokens.get(4)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(5)).isEqualTo(new Token("5", TokenType.QUANTITY));
        assertThat(tokens.get(6)).isEqualTo(new Token(".", TokenType.OPERAND));
        assertThat(tokens.get(7)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.get(8)).isEqualTo(new Token("substitute", TokenType.ALTERNATIVE));
        assertThat(tokens.get(9)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(10)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(11)).isEqualTo(new Token("honey", TokenType.OTHER));
        assertThat(tokens.get(12)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(13)).isEqualTo(new Token("tbsp", TokenType.UNIT));
        assertThat(tokens.get(14)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(15)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(16);
    }

    @Test
    public void testTokenStackPrepositionCombinePrevAndNext() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("200g pork belly with skin scored crosswise into 1 inch squares");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("scored crosswise into 1 inch squares", TokenType.STATE));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("pork belly with skin", TokenType.OTHER));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("200", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackIgnorePrepositionPrefixAmount() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1/2 of an onion");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("onion", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("an", TokenType.UNIT));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("2", TokenType.QUANTITY));
        assertThat(tokens.get(5)).isEqualTo(new Token("/", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackIgnorePrepositionPrefixUnit() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1/2 tsp of sugar");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("sugar", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("tsp", TokenType.UNIT));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("2", TokenType.QUANTITY));
        assertThat(tokens.get(5)).isEqualTo(new Token("/", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackAlternativeWithoutBrackets() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("500g apple or pear");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("pear", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token("or", TokenType.ALTERNATIVE));
        assertThat(tokens.get(2)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("500", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackComplexAdditionalInfo() throws Exception {
        String ingredientString = "400g peeled apple (14.1 ounces) or 500g peeled pear (17.6 ounces)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("ounces", TokenType.UNIT));
        assertThat(tokens.get(2)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(3)).isEqualTo(new Token("6", TokenType.QUANTITY));
        assertThat(tokens.get(4)).isEqualTo(new Token(".", TokenType.OPERAND));
        assertThat(tokens.get(5)).isEqualTo(new Token("17", TokenType.QUANTITY));
        assertThat(tokens.get(6)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(7)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("pear", TokenType.OTHER));
        assertThat(tokens.get(9)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(10)).isEqualTo(new Token("peeled", TokenType.STATE));
        assertThat(tokens.get(11)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(12)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(13)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(14)).isEqualTo(new Token("500", TokenType.QUANTITY));
        assertThat(tokens.get(15)).isEqualTo(new Token("or", TokenType.ALTERNATIVE));
        assertThat(tokens.get(16)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(17)).isEqualTo(new Token("ounces", TokenType.UNIT));
        assertThat(tokens.get(18)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(19)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.get(20)).isEqualTo(new Token(".", TokenType.OPERAND));
        assertThat(tokens.get(21)).isEqualTo(new Token("14", TokenType.QUANTITY));
        assertThat(tokens.get(22)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(23)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(24)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(25)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(26)).isEqualTo(new Token("peeled", TokenType.STATE));
        assertThat(tokens.get(27)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(28)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(29)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(30)).isEqualTo(new Token("400", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(31);
    }

    @Test
    public void testTokenStackSeriesOfAlternatives() throws Exception {
        String ingredientString = "3.5 kg minced lamb or beef or chicken";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("chicken", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token("or", TokenType.ALTERNATIVE));
        assertThat(tokens.get(2)).isEqualTo(new Token("beef", TokenType.OTHER));
        assertThat(tokens.get(3)).isEqualTo(new Token("or", TokenType.ALTERNATIVE));
        assertThat(tokens.get(4)).isEqualTo(new Token("lamb", TokenType.OTHER));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("minced", TokenType.STATE));
        assertThat(tokens.get(7)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("kg", TokenType.UNIT));
        assertThat(tokens.get(9)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(10)).isEqualTo(new Token("5", TokenType.QUANTITY));
        assertThat(tokens.get(11)).isEqualTo(new Token(".", TokenType.OPERAND));
        assertThat(tokens.get(12)).isEqualTo(new Token("3", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(13);
    }

    @Test
    public void testTokenStackMultipleStates() throws WrongTokenTypeException {
        String ingredientString = "1 peeled apple, finely diced";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("finely diced", TokenType.STATE));
        assertThat(tokens.get(1)).isEqualTo(new Token(",", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("peeled", TokenType.STATE));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }


    @Test
    public void testTokenStackMultipleMeasurements() throws WrongTokenTypeException {
        String ingredientString = "1 apple (200g, 7oz)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("oz", TokenType.UNIT));
        assertThat(tokens.get(2)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(3)).isEqualTo(new Token("7", TokenType.QUANTITY));
        assertThat(tokens.get(4)).isEqualTo(new Token(",", TokenType.OPERAND));
        assertThat(tokens.get(5)).isEqualTo(new Token("g", TokenType.UNIT));
        assertThat(tokens.get(6)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(7)).isEqualTo(new Token("200", TokenType.QUANTITY));
        assertThat(tokens.get(8)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(9)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(10)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(11)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(12)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(13);
    }


    @Test
    public void testTokenStackMeasurementRange() throws WrongTokenTypeException {
        String ingredientString = "1-3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("apples", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("3", TokenType.QUANTITY));
        assertThat(tokens.get(3)).isEqualTo(new Token("-", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(5);
    }


    @Test
    public void testTokenStackHybridFraction() throws WrongTokenTypeException {
        String ingredientString = "1+1/3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("apples", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("3", TokenType.QUANTITY));
        assertThat(tokens.get(3)).isEqualTo(new Token("/", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.get(5)).isEqualTo(new Token("+", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }


    @Test
    public void testTokenStackUglyFraction() throws WrongTokenTypeException {
        String ingredientString = "1.1+1.5/3 apples";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("apples", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("3", TokenType.QUANTITY));
        assertThat(tokens.get(3)).isEqualTo(new Token("/", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("5", TokenType.QUANTITY));
        assertThat(tokens.get(5)).isEqualTo(new Token(".", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.get(7)).isEqualTo(new Token("+", TokenType.OPERAND));
        assertThat(tokens.get(8)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.get(9)).isEqualTo(new Token(".", TokenType.OPERAND));
        assertThat(tokens.get(10)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(11);
    }

    @Test
    public void testTokenStackOrBetweenStates() throws WrongTokenTypeException {
        String ingredientString = "1 chopped or diced apple";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("diced", TokenType.STATE));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("chopped", TokenType.STATE));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackOrBetweenStatesComma() throws WrongTokenTypeException {
        String ingredientString = "1 apple, chopped or diced";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("diced", TokenType.STATE));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("chopped", TokenType.STATE));
        assertThat(tokens.get(3)).isEqualTo(new Token(",", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackOrInBrackets() throws WrongTokenTypeException {
        String ingredientString = "1 apple (or pear)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("pear", TokenType.OTHER));
        assertThat(tokens.get(2)).isEqualTo(new Token("or", TokenType.ALTERNATIVE));
        assertThat(tokens.get(3)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(4)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(5)).isEqualTo(new Token("apple", TokenType.OTHER));
        assertThat(tokens.get(6)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(7)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(8);
    }

    @Test
    public void testTokenStackDoubleOrInBrackets() throws WrongTokenTypeException {
        String ingredientString = "1 apple (or pear or pineapple)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")",TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("pineapple",TokenType.OTHER));
        assertThat(tokens.get(2)).isEqualTo(new Token("or",TokenType.ALTERNATIVE));
        assertThat(tokens.get(3)).isEqualTo(new Token("pear",TokenType.OTHER));
        assertThat(tokens.get(4)).isEqualTo(new Token("or",TokenType.ALTERNATIVE));
        assertThat(tokens.get(5)).isEqualTo(new Token("(",TokenType.OPEN_BRACKET));
        assertThat(tokens.get(6)).isEqualTo(new Token(" ",TokenType.OPERAND));
        assertThat(tokens.get(7)).isEqualTo(new Token("apple",TokenType.OTHER));
        assertThat(tokens.get(8)).isEqualTo(new Token(" ",TokenType.OPERAND));
        assertThat(tokens.get(9)).isEqualTo(new Token("1",TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(10);
    }

    @Test
    public void testTokenStackAlternativeIngredientWithoutToken() throws WrongTokenTypeException {
        String ingredientString = "1 bread bun (kaiser bun)";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token(")", TokenType.CLOSE_BRACKET));
        assertThat(tokens.get(1)).isEqualTo(new Token("kaiser bun", TokenType.OTHER));
        assertThat(tokens.get(2)).isEqualTo(new Token("(", TokenType.OPEN_BRACKET));
        assertThat(tokens.get(3)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(4)).isEqualTo(new Token("bread bun", TokenType.OTHER));
        assertThat(tokens.get(5)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(6)).isEqualTo(new Token("1", TokenType.QUANTITY));
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testTokenStackNoConcreteAmount() throws WrongTokenTypeException {
        String ingredientString = "some salt";
        IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0)).isEqualTo(new Token("salt", TokenType.OTHER));
        assertThat(tokens.get(1)).isEqualTo(new Token(" ", TokenType.OPERAND));
        assertThat(tokens.get(2)).isEqualTo(new Token("some", TokenType.UNIT));
        assertThat(tokens.size()).isEqualTo(3);
    }
}

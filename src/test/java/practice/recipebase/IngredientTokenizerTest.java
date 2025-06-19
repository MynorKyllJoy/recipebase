package practice.recipebase;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import practice.recipebase.interpreter.IngredientTokenizer;
import practice.recipebase.interpreter.Token;

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
        assertThat(quantityToken.value()).isEqualTo("1");
        assertThat(quantityToken.type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokenizer.size()).isEqualTo(2);

        Token operandToken = tokenizer.next();
        assertThat(operandToken.value()).isEqualTo(" ");
        assertThat(operandToken.type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokenizer.size()).isEqualTo(1);

        Token ingredientToken = tokenizer.next();
        assertThat(ingredientToken.value()).isEqualTo("carrot");
        assertThat(ingredientToken.type()).isEqualTo(TokenType.OTHER);
        assertThat(tokenizer.size()).isEqualTo(0);

        Token endToken = tokenizer.next();
        assertThat(endToken.type()).isEqualTo(TokenType.END);
        assertThat(endToken.value()).isEqualTo("");
        assertThat(tokenizer.size()).isEqualTo(0);
    }


    @Test
    public void testPeek() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 carrot");

        Token quantityToken = tokenizer.peek();
        assertThat(quantityToken.value()).isEqualTo("1");
        assertThat(quantityToken.type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokenizer.size()).isEqualTo(3);
        assertThat(quantityToken).isEqualTo(tokenizer.next());

        Token operandToken = tokenizer.peek();
        assertThat(operandToken.value()).isEqualTo(" ");
        assertThat(operandToken.type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokenizer.size()).isEqualTo(2);
        assertThat(operandToken).isEqualTo(tokenizer.next());

        Token ingredientToken = tokenizer.peek();
        assertThat(ingredientToken.value()).isEqualTo("carrot");
        assertThat(ingredientToken.type()).isEqualTo(TokenType.OTHER);
        assertThat(tokenizer.size()).isEqualTo(1);
        assertThat(ingredientToken).isEqualTo(tokenizer.next());

        Token endToken = tokenizer.peek();
        assertThat(endToken.type()).isEqualTo(TokenType.END);
        assertThat(endToken.value()).isEqualTo("");
        assertThat(tokenizer.size()).isEqualTo(0);
        assertThat(endToken).isEqualTo(tokenizer.next());
    }

    @Test
    public void testToListSimple() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("3 diced potato");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo("potato");
        assertThat(tokens.get(1).value()).isEqualTo(" ");
        assertThat(tokens.get(2).value()).isEqualTo("diced");
        assertThat(tokens.get(3).value()).isEqualTo(" ");
        assertThat(tokens.get(4).value()).isEqualTo("3");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(5);
    }

    @Test
    public void testToListCommaOperand() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("3 medium onion, chopped");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo("chopped");
        assertThat(tokens.get(1).value()).isEqualTo(",");
        assertThat(tokens.get(2).value()).isEqualTo("onion");
        assertThat(tokens.get(3).value()).isEqualTo(" ");
        assertThat(tokens.get(4).value()).isEqualTo("medium");
        assertThat(tokens.get(5).value()).isEqualTo(" ");
        assertThat(tokens.get(6).value()).isEqualTo("3");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(7);
    }

    @Test
    public void testToListDifferentOrder() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("ginger, grated, 1\" knob");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo("knob");
        assertThat(tokens.get(1).value()).isEqualTo(" ");
        assertThat(tokens.get(2).value()).isEqualTo("\"");
        assertThat(tokens.get(3).value()).isEqualTo(" ");
        assertThat(tokens.get(4).value()).isEqualTo("1");
        assertThat(tokens.get(5).value()).isEqualTo(",");
        assertThat(tokens.get(6).value()).isEqualTo("grated");
        assertThat(tokens.get(7).value()).isEqualTo(",");
        assertThat(tokens.get(8).value()).isEqualTo("ginger");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(7).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(8).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.size()).isEqualTo(9);
    }

    @Test
    public void testToListBindingWord() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("100g pork sausage (chopped into small pieces)");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo(")");
        assertThat(tokens.get(1).value()).isEqualTo("chopped into small pieces");
        assertThat(tokens.get(2).value()).isEqualTo("(");
        assertThat(tokens.get(3).value()).isEqualTo("pork sausage");
        assertThat(tokens.get(4).value()).isEqualTo(" ");
        assertThat(tokens.get(5).value()).isEqualTo("g");
        assertThat(tokens.get(6).value()).isEqualTo(" ");
        assertThat(tokens.get(7).value()).isEqualTo("100");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.CLOSE_BRACKET);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.OPEN_BRACKET);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(7).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(8);
    }

    @Test
    public void testToListBrackets() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 medium onion (140g)");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo(")");
        assertThat(tokens.get(1).value()).isEqualTo("g");
        assertThat(tokens.get(2).value()).isEqualTo(" ");
        assertThat(tokens.get(3).value()).isEqualTo("140");
        assertThat(tokens.get(4).value()).isEqualTo("(");
        assertThat(tokens.get(5).value()).isEqualTo("onion");
        assertThat(tokens.get(6).value()).isEqualTo(" ");
        assertThat(tokens.get(7).value()).isEqualTo("medium");
        assertThat(tokens.get(8).value()).isEqualTo(" ");
        assertThat(tokens.get(9).value()).isEqualTo("1");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.CLOSE_BRACKET);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.OPEN_BRACKET);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(7).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(8).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(9).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(10);
    }

    @Test
    public void testToListAlternativeIngredient() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 medium red onion (140g) or white onion");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo("white onion");
        assertThat(tokens.get(1).value()).isEqualTo("or");
        assertThat(tokens.get(2).value()).isEqualTo(")");
        assertThat(tokens.get(3).value()).isEqualTo("g");
        assertThat(tokens.get(4).value()).isEqualTo(" ");
        assertThat(tokens.get(5).value()).isEqualTo("140");
        assertThat(tokens.get(6).value()).isEqualTo("(");
        assertThat(tokens.get(7).value()).isEqualTo("red onion");
        assertThat(tokens.get(8).value()).isEqualTo(" ");
        assertThat(tokens.get(9).value()).isEqualTo("medium");
        assertThat(tokens.get(10).value()).isEqualTo(" ");
        assertThat(tokens.get(11).value()).isEqualTo("1");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.CLOSE_BRACKET);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.OPEN_BRACKET);
        assertThat(tokens.get(7).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(8).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(9).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(10).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(11).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(12);
    }

    @Test
    public void testToListPrepositionCombinePrev() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1 tbsp honey (substitute with 1.5 tbsp sugar)");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo(")");
        assertThat(tokens.get(1).value()).isEqualTo("sugar");
        assertThat(tokens.get(2).value()).isEqualTo(" ");
        assertThat(tokens.get(3).value()).isEqualTo("tbsp");
        assertThat(tokens.get(4).value()).isEqualTo(" ");
        assertThat(tokens.get(5).value()).isEqualTo("5");
        assertThat(tokens.get(6).value()).isEqualTo(".");
        assertThat(tokens.get(7).value()).isEqualTo("1");
        assertThat(tokens.get(8).value()).isEqualTo("substitute with");
        assertThat(tokens.get(9).value()).isEqualTo("(");
        assertThat(tokens.get(10).value()).isEqualTo("honey");
        assertThat(tokens.get(11).value()).isEqualTo(" ");
        assertThat(tokens.get(12).value()).isEqualTo("tbsp");
        assertThat(tokens.get(13).value()).isEqualTo(" ");
        assertThat(tokens.get(14).value()).isEqualTo("1");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.CLOSE_BRACKET);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(7).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(8).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(9).type()).isEqualTo(TokenType.OPEN_BRACKET);
        assertThat(tokens.get(10).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(11).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(12).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(13).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(14).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(15);
    }

    @Test
    public void testToListPrepositionCombinePrevAndNext() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("200g pork belly with skin scored crosswise into 1 inch cubes");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo("cubes");
        assertThat(tokens.get(1).value()).isEqualTo(" ");
        assertThat(tokens.get(2).value()).isEqualTo("inch");
        assertThat(tokens.get(3).value()).isEqualTo(" ");
        assertThat(tokens.get(4).value()).isEqualTo("1");
        assertThat(tokens.get(5).value()).isEqualTo(" ");
        assertThat(tokens.get(6).value()).isEqualTo("scored crosswise into");
        assertThat(tokens.get(7).value()).isEqualTo(" ");
        assertThat(tokens.get(8).value()).isEqualTo("pork belly with skin");
        assertThat(tokens.get(9).value()).isEqualTo(" ");
        assertThat(tokens.get(10).value()).isEqualTo("g");
        assertThat(tokens.get(11).value()).isEqualTo(" ");
        assertThat(tokens.get(12).value()).isEqualTo("200");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.STATE);
        assertThat(tokens.get(7).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(8).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(9).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(10).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(11).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(12).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(13);
    }

    @Test
    public void testToListIgnorePreposition() {
        IngredientTokenizer tokenizer = new IngredientTokenizer("1/2 of an onion");
        List<Token> tokens = tokenizer.toList();

        assertThat(tokens.get(0).value()).isEqualTo("onion");
        assertThat(tokens.get(1).value()).isEqualTo(" ");
        assertThat(tokens.get(2).value()).isEqualTo("an");
        assertThat(tokens.get(3).value()).isEqualTo(" ");
        assertThat(tokens.get(4).value()).isEqualTo("2");
        assertThat(tokens.get(5).value()).isEqualTo("/");
        assertThat(tokens.get(6).value()).isEqualTo("1");
        assertThat(tokens.get(0).type()).isEqualTo(TokenType.OTHER);
        assertThat(tokens.get(1).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(2).type()).isEqualTo(TokenType.UNIT);
        assertThat(tokens.get(3).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(4).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.get(5).type()).isEqualTo(TokenType.OPERAND);
        assertThat(tokens.get(6).type()).isEqualTo(TokenType.QUANTITY);
        assertThat(tokens.size()).isEqualTo(7);
    }
}

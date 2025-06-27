package practice.recipebase;

import org.junit.jupiter.api.Test;
import practice.recipebase.misc.IngredientCleaner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IngredientCleanerTest {
    @Test
    public void testFixHybridFractions() {
        String result = new IngredientCleaner("2⅒").fixHybridFractions().getCleanedIngredient();
        assertThat(result).isEqualTo("2+⅒");
    }

    @Test
    public void testFixHybridFractionsWithSpace() {
        String result = new IngredientCleaner("20 ⅚").fixHybridFractions().getCleanedIngredient();
        assertThat(result).isEqualTo("20+⅚");
    }

    @Test
    public void testReplaceUnicode() {
        String testString = "½ ⅓ ⅔ ¼ ¾ ⅕ ⅖ ⅗ ⅘ ⅙ ⅚ ⅐ ⅛ ⅜ ⅝ ⅞ ⅑ ⅒ 123456/7890 – —";
        String result = new IngredientCleaner(testString).replaceUnicode().getCleanedIngredient();
        String expected = "1/2 1/3 2/3 1/4 3/4 1/5 2/5 3/5 4/5 1/6 5/6 1/7 1/8 3/8 5/8 7/8 1/9 1/10 123456/7890 - -";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testRemoveOxfordComma() {
        String testString = "Apples, Bananas, and Pears. Pork, Chicken, or Beef. A coke, plus lemon. A, B, or C.";
        String expected = "Apples, Bananas and Pears. Pork, Chicken or Beef. A coke plus lemon. A, B or C.";
        String result = new IngredientCleaner(testString).removeOxfordComma().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testReplaceEnumerationCommaToOr() {
        String testString = "Beef, Chicken, Lamb, Pork or Turkey";
        String expected = "Beef or Chicken or Lamb or Pork or Turkey";
        String result = new IngredientCleaner(testString).replaceEnumerationCommaToOr().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testReplaceAndSlashOrWithOr() {
        String testString = "and/or";
        String expected = "or";
        String result = new IngredientCleaner(testString).replaceAndSlashOrWithOr().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testUseHyphenForWords() {
        String testString = "3-4 bite-size, hard-boiled and soft-boiled eggs";
        String expected = "3-4 bite‐size, hard‐boiled and soft‐boiled eggs";
        String result = new IngredientCleaner(testString).useHyphenForWords().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testRemoveNonASCII() {
        String testString = "Ich fand das pökeln vom Schweinefleisch ätzend. Es gibt einen Unterschied zwischen - & ‐";
        String expected = "Ich fand das pkeln vom Schweinefleisch tzend. Es gibt einen Unterschied zwischen - & ";
        String result = new IngredientCleaner(testString).removeNonASCII().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCleanBrackets() {
        String testString = "(,) (aaaa ,  ) (aaaa ,) (, aaaa) (,aaaa,) ( aaaa )";
        String expected = "() (aaaa) (aaaa) (aaaa) (aaaa) ( aaaa )";
        String result = new IngredientCleaner(testString).cleanBrackets().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testRemoveUnneededComments() {
        String testString = "see note, or more, more, around, about, if unavailable, I used X brand, you can skip, "
                + "for garnish, as garnish";
        String expected = ", , , , , , , , , ";
        String result = new IngredientCleaner(testString).removeUnneededComments().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testRemoveEmptyBrackets() {
        String testString = "100g Chocolate (), chopped";
        String expected = "100g Chocolate , chopped";
        String result = new IngredientCleaner(testString).removeEmptyBrackets().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testReplaceSlashesNextToWordsWithComma() {
        String testString = "1 piece of bread (Kaiserrolle/Baguette/Ciabatta)";
        String expected = "1 piece of bread (Kaiserrolle,Baguette,Ciabatta)";
        String result = new IngredientCleaner(testString).replaceSlashesNextToWordsWithComma().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testReplaceToWithMinusForRanges() {
        String testString = "100 to 200g chicken stock";
        String expected = "100-200g chicken stock";
        String result = new IngredientCleaner(testString).replaceToWithMinusForRanges().getCleanedIngredient();
        assertThat(result).isEqualTo(expected);
    }
}

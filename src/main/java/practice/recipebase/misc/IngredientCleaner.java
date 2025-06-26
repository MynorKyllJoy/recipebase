package practice.recipebase.misc;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngredientCleaner {
    @Getter
    private String cleanedIngredient;
    private final Map<String, String> unicodeFractions;
    private final Map<String, String> otherUnicodeReplacements;

    public IngredientCleaner(String ingredient) {
        this.cleanedIngredient = ingredient;
        this.unicodeFractions = new HashMap<>();
        this.otherUnicodeReplacements = new HashMap<>();
        // fractions
        unicodeFractions.put("½", "1/2");
        unicodeFractions.put("⅓", "1/3");
        unicodeFractions.put("⅔", "2/3");
        unicodeFractions.put("¼", "1/4");
        unicodeFractions.put("¾", "3/4");
        unicodeFractions.put("⅕", "1/5");
        unicodeFractions.put("⅖", "2/5");
        unicodeFractions.put("⅗", "3/5");
        unicodeFractions.put("⅘", "4/5");
        unicodeFractions.put("⅙", "1/6");
        unicodeFractions.put("⅚", "5/6");
        unicodeFractions.put("⅐", "1/7");
        unicodeFractions.put("⅛", "1/8");
        unicodeFractions.put("⅜", "3/8");
        unicodeFractions.put("⅝", "5/8");
        unicodeFractions.put("⅞", "7/8");
        unicodeFractions.put("⅑", "1/9");
        unicodeFractions.put("⅒", "1/10");
        // dash
        otherUnicodeReplacements.put("–", "-");
        otherUnicodeReplacements.put("—", "-");
    }

    public IngredientCleaner fixHybridFractions() {
        // turns hybrid fractions into a math formula: 1½ -> 1+½ and 1 ½ -> 1+½
        for(String unicodeFraction : unicodeFractions.keySet()) {
            Pattern pattern = Pattern.compile("(?<=[0-9]) ?(?=" + unicodeFraction + ")");
            this.cleanedIngredient = pattern.matcher(this.cleanedIngredient).replaceAll("+");
        }
        return this;

    }

    public IngredientCleaner replaceUnicode() {
        unicodeFractions.forEach(
                (key, value) -> this.cleanedIngredient = this.cleanedIngredient.replace(key, value)
        );
        otherUnicodeReplacements.forEach(
                (key, value) -> this.cleanedIngredient = this.cleanedIngredient.replace(key, value)
        );
        return this;
    }

    public IngredientCleaner removeOxfordComma() {
        String[] conjunctions = {"and", "or", "plus"};
        for(String conjunction : conjunctions) {
            Pattern pattern = Pattern.compile(",(?= " + conjunction + ")");
            this.cleanedIngredient = pattern.matcher(this.cleanedIngredient).replaceAll("");
        }
        return this;
    }

    public IngredientCleaner replaceEnumerationCommaToOr() {
        Pattern pattern = Pattern.compile("(?<=[a-zA-Z]), (?=[a-zA-Z ]+ or)");
        Matcher matcher = pattern.matcher(this.cleanedIngredient);
        while(matcher.find()) {
            this.cleanedIngredient = matcher.replaceAll(" or ");
            matcher = pattern.matcher(this.cleanedIngredient);
        }
        return this;
    }

    public IngredientCleaner replaceAndSlashOrWithOr() {
        this.cleanedIngredient = this.cleanedIngredient.replace("and/or", "or");
        return this;
    }

    public IngredientCleaner useHyphenForWords() {
        // turn minus between two words into a hyphen
        Pattern pattern = Pattern.compile("((?<=[a-zA-Z])-(?=[a-zA-Z]))");
        Matcher matcher = pattern.matcher(this.cleanedIngredient);
        while(matcher.find()) {
            // \u2010 (hyphen) and \u002D (minus) are nearly indistinguishable
            this.cleanedIngredient = matcher.replaceAll("‐");
            matcher = pattern.matcher(this.cleanedIngredient);
        }
        return this;
    }

    public IngredientCleaner removeNonASCII() {
        this.cleanedIngredient = this.cleanedIngredient.replaceAll("[^\\x20-\\x7E]", "");
        return this;
    }

    public IngredientCleaner removeUnneededComments() {
        String[] comments = {
                "see note", "(or )?more", "around", "about", "if [a-zA-Z]+",
                "([iI] [a-zA-Z ]+)", "([yY]ou [a-zA-Z ]+)"
        };
        for(String comment : comments) {
            this.cleanedIngredient = this.cleanedIngredient.replaceAll(comment, "");
        }
        return this;
    }

    public IngredientCleaner cleanBrackets() {
        this.cleanedIngredient = this.cleanedIngredient.replaceAll("\\( *, *", "(");
        this.cleanedIngredient = this.cleanedIngredient.replaceAll(" *, *\\)", ")");
        return this;
    }

    public IngredientCleaner removeEmptyBrackets() {
        this.cleanedIngredient = this.cleanedIngredient.replaceAll("\\(\\)", "");
        this.cleanedIngredient = this.cleanedIngredient.replaceAll("\\[]", "");
        this.cleanedIngredient = this.cleanedIngredient.replaceAll("\\{}", "");
        return this;
    }

    public IngredientCleaner replaceSlashesNextToWordsWithComma() {
        Pattern slashAfterWord = Pattern.compile("(?<=[a-zA-Z]) ?/");
        Pattern slashBeforeWord = Pattern.compile("/ ?(?=[a-zA-Z])");
        this.cleanedIngredient = slashAfterWord.matcher(this.cleanedIngredient).replaceAll(",");
        this.cleanedIngredient = slashBeforeWord.matcher(this.cleanedIngredient).replaceAll(",");
        return this;
    }

    public IngredientCleaner replaceToWithMinusForRanges() {
        Pattern slashAfterWord = Pattern.compile("(?<=[0-9]) to (?=[0-9])");
        this.cleanedIngredient = slashAfterWord.matcher(this.cleanedIngredient).replaceAll("-");
        return this;
    }
}

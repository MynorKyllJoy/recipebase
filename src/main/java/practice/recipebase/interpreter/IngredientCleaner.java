package practice.recipebase.interpreter;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngredientCleaner {
    @Getter
    private String cleanedIngredient;
    private final Map<String, String> unicodeReplacement;

    public IngredientCleaner(String ingredient) {
        this.cleanedIngredient = ingredient;
        this.unicodeReplacement = new HashMap<>();
        // fractions
        unicodeReplacement.put("½", "1/2");
        unicodeReplacement.put("⅓", "1/3");
        unicodeReplacement.put("⅔", "2/3");
        unicodeReplacement.put("¼", "1/4");
        unicodeReplacement.put("¾", "3/4");
        unicodeReplacement.put("⅕", "1/5");
        unicodeReplacement.put("⅖", "2/5");
        unicodeReplacement.put("⅗", "3/5");
        unicodeReplacement.put("⅘", "4/5");
        unicodeReplacement.put("⅙", "1/6");
        unicodeReplacement.put("⅚", "5/6");
        unicodeReplacement.put("⅐", "1/7");
        unicodeReplacement.put("⅛", "1/8");
        unicodeReplacement.put("⅜", "3/8");
        unicodeReplacement.put("⅝", "5/8");
        unicodeReplacement.put("⅞", "7/8");
        unicodeReplacement.put("⅑", "1/9");
        unicodeReplacement.put("⅒", "1/10");
        // dash
        unicodeReplacement.put("–", "-");
        unicodeReplacement.put("—", "-");
    }

    public IngredientCleaner replaceUnicode() {
        unicodeReplacement.forEach(
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

    public IngredientCleaner enumerationCommaToOr() {
        Pattern pattern = Pattern.compile("(?=[a-zA-Z]),\\s(?=[a-zA-Z\\s]+\\sor)");
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

    public IngredientCleaner cleanBrackets() {
        this.cleanedIngredient = this.cleanedIngredient.replaceAll("\\(,", "(");
        this.cleanedIngredient = this.cleanedIngredient.replaceAll(",\\)", ")");
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
}

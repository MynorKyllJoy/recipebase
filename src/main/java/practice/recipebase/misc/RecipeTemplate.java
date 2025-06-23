package practice.recipebase.misc;

import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.interpreter.IngredientParser;
import practice.recipebase.interpreter.IngredientTokenizer;
import practice.recipebase.model.Recipe;
import practice.recipebase.model.Requirement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract class RecipeTemplate {
    // Template pattern since I want to save scraped and self-made recipes in the database
    public Recipe createRecipe() throws WrongTokenTypeException {
        try {
            String title = this.getTitle();
            String description = this.getDescription();
            String source = this.getSource();
            List<String> ingredientInfos = this.getIngredientInfos();
            ingredientInfos = this.cleanIngredientInfos(ingredientInfos);
            List<Requirement> requiredIngredients = this.getRequirements(ingredientInfos);
            List<String> instructions = this.getInstructions();
            return new Recipe(title, description, source, requiredIngredients, instructions);
        } catch (IOException ex) {
            return null;
        }
    }

    private List<String> cleanIngredientInfos(List<String> ingredientInfos) {
        List<String> cleanedIngredients = new ArrayList<>();
        for(String info : ingredientInfos) {
            IngredientCleaner cleaner = new IngredientCleaner(info);
            String cleanedInfo = cleaner
                    .removeOxfordComma()
                    .replaceUnicode() // transform all Unicode characters we want to preserve
                    .removeNonASCII() // remove all remaining Unicode characters
                    .cleanBrackets()
                    .removeEmptyBrackets()
                    .replaceAndSlashOrWithOr()
                    .replaceSlashesNextToWordsWithComma()
                    .enumerationCommaToOr()
                    .useHyphenForWords() // add hyphen as Unicode character
                    .replaceToWithMinusForRanges()
                    .getCleanedIngredient();
            cleanedIngredients.add(cleanedInfo);
        }
        return cleanedIngredients;
    }

    private List<Requirement> getRequirements(List<String> ingredientInfos) throws WrongTokenTypeException {
        List<Requirement> requiredIngredients = new ArrayList<>();
        for(String ingredientString : ingredientInfos) {
            IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
            System.out.println(tokenizer.toList());
            IngredientParser parser = new IngredientParser(tokenizer, 0);
            requiredIngredients.addAll(parser.parse().interpret().getRequirements());
        }
        return requiredIngredients;
    }

    abstract String getTitle();
    abstract String getDescription();
    abstract String getSource();
    abstract List<String> getIngredientInfos() throws IOException;
    abstract List<String> getInstructions() throws IOException;
}

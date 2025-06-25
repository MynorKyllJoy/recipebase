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
            // get simple information
            String title = this.getTitle();
            String description = this.getDescription();
            String source = this.getSource();

            // process ingredient information
            List<String> ingredientInfos = this.getIngredientInfos();
            ingredientInfos = this.cleanIngredientInfos(ingredientInfos);
            System.out.println(ingredientInfos);
            List<Requirement> requiredIngredients = this.getRequirements(ingredientInfos);
            // get instruction information
            List<String> instructions = this.getInstructions();
            return new Recipe(title, description, source, requiredIngredients, instructions);
        } catch (IOException ex) {
            return null;
        }
    }

    private List<String> cleanIngredientInfos(List<String> ingredientInfos) {
        // transform string into a format that works with parser
        List<String> cleanedIngredients = new ArrayList<>();
        for(String info : ingredientInfos) {
            IngredientCleaner cleaner = new IngredientCleaner(info);
            String cleanedInfo = cleaner
                    .removeOxfordComma()
                    .fixHybridFractions() // turns 1½ and 1 ½ into 1+½
                    .replaceUnicode() // transform all Unicode characters we want to preserve
                    .removeNonASCII() // remove all remaining Unicode characters
                    .removeUnneededComments()
                    .cleanBrackets()
                    .removeEmptyBrackets()
                    .replaceAndSlashOrWithOr()
                    .replaceSlashesNextToWordsWithComma()
                    .replaceEnumerationCommaToOr()
                    .useHyphenForWords() // add hyphen as Unicode character
                    .replaceToWithMinusForRanges()
                    .getCleanedIngredient();
            cleanedIngredients.add(cleanedInfo);
        }
        return cleanedIngredients;
    }

    private List<Requirement> getRequirements(List<String> ingredientInfos) throws WrongTokenTypeException {
        // tokenize and parse string to get the necessary requirements
        List<Requirement> requiredIngredients = new ArrayList<>();
        for(String ingredientString : ingredientInfos) {
            IngredientTokenizer tokenizer = new IngredientTokenizer(ingredientString);
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

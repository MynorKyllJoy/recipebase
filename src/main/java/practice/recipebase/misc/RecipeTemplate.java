package practice.recipebase.misc;

import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;

import java.io.IOException;
import java.util.List;

abstract class RecipeTemplate {
    // Template pattern since I want to save scraped and self-made recipes in the database
    public Recipe createRecipe() {
        try {
            String title = this.getTitle();
            String description = this.getDescription();
            String source = this.getSource();
            List<Ingredient> ingredients = this.getIngredients();
            List<String> instructions = this.getInstructions();
            return new Recipe(title, description, source, ingredients, instructions);
        } catch (IOException ex) {
            return null;
        }
    }

    abstract String getTitle();
    abstract String getDescription();
    abstract String getSource();
    abstract List<Ingredient> getIngredients() throws IOException;
    abstract List<String> getInstructions() throws IOException;
}

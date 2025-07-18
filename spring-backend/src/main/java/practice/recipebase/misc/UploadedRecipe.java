package practice.recipebase.misc;

import java.io.IOException;
import java.util.List;

public class UploadedRecipe extends RecipeTemplate {
    UploadedRecipeWrapper recipeWrapper;

    public UploadedRecipe(UploadedRecipeWrapper recipeWrapper) {
        this.recipeWrapper = recipeWrapper;
    }

    @Override
    String getTitle() {
        return recipeWrapper.getTitle();
    }

    @Override
    String getDescription() {
        return recipeWrapper.getDescription();
    }

    @Override
    String getSource() {
        return recipeWrapper.getUserID();
    }

    @Override
    List<String> getIngredientInfos() throws IOException {
        return recipeWrapper.getIngredients();
    }

    @Override
    List<String> getInstructions() throws IOException {
        return recipeWrapper.getInstructions();
    }
}

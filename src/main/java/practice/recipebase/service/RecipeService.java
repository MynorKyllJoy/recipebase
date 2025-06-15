package practice.recipebase.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import practice.recipebase.misc.RecipeSiteRequestAdapter;
import practice.recipebase.misc.ScrapedRecipe;
import practice.recipebase.model.Recipe;

import java.io.IOException;

@Service
public class RecipeService {
    public Recipe getRecipeMetaData(String URL) throws IOException {
        Document recipeSite = RecipeSiteRequestAdapter.getRecipeSite(URL);
        ScrapedRecipe scrapedRecipe = new ScrapedRecipe(recipeSite, URL);
        return scrapedRecipe.createRecipe();
    }
}

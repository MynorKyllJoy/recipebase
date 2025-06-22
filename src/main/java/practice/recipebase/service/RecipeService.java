package practice.recipebase.service;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.recipebase.exceptions.RecipeAlreadyExistsException;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.misc.RecipeSiteRequestAdapter;
import practice.recipebase.misc.ScrapedRecipe;
import practice.recipebase.model.Recipe;
import practice.recipebase.repository.RecipeRepository;

import java.io.IOException;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    public Recipe getRecipeMetaData(String URL) throws IOException, WrongTokenTypeException, RecipeAlreadyExistsException {
        if(!recipeRepository.findRecipeBySource(URL).isEmpty()) {
            throw new RecipeAlreadyExistsException("The recipe you are trying to scrape is already in the database.");
        }
        Document recipeSite = RecipeSiteRequestAdapter.getRecipeSite(URL);
        ScrapedRecipe scrapedRecipe = new ScrapedRecipe(recipeSite, URL);
        return scrapedRecipe.createRecipe();
    }

    public void save(Recipe scrapedRecipe) {
        recipeRepository.save(scrapedRecipe);
    }
}

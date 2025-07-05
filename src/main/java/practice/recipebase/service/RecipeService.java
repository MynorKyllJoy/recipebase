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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    public Recipe getRecipeMetaData(String URL) throws IOException, WrongTokenTypeException, RecipeAlreadyExistsException {
        if(!this.getRecipeBySource(URL).isEmpty()) {
            throw new RecipeAlreadyExistsException("The recipe you are trying to scrape is already in the database.");
        }
        Document recipeSite = RecipeSiteRequestAdapter.getRecipeSite(URL);
        ScrapedRecipe scrapedRecipe = new ScrapedRecipe(recipeSite, URL);
        return scrapedRecipe.createRecipe();
    }

    public Recipe saveRecipe(Recipe scrapedRecipe) {
        return recipeRepository.save(scrapedRecipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String recipeId) {
        return recipeRepository.findById(recipeId).orElse(new Recipe());
    }

    public List<Recipe> getRecipeBySource(String url) {
        return recipeRepository.findRecipeBySource(url);
    }

    public Set<Recipe> getRecipeByConditions(List<String> ingredientNames) {
        return new HashSet<>(recipeRepository.findRecipeByIngredientRequirements(ingredientNames));
    }
}

package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.recipebase.exceptions.RecipeAlreadyExistsException;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.misc.IngredientsWrapper;
import practice.recipebase.misc.UploadedRecipeWrapper;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;
import practice.recipebase.service.IngredientService;
import practice.recipebase.service.RecipeService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "http://localhost:5173")
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    IngredientService ingredientService;

    @GetMapping("/all")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/ingredients")
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @PostMapping("/upload")
    public Recipe uploadRecipe(@RequestBody UploadedRecipeWrapper recipeWrapper)
            throws WrongTokenTypeException {
        Recipe uploadedRecipe = recipeService.createUploadedRecipe(recipeWrapper);
        return recipeService.saveRecipe(uploadedRecipe);
    }

    @PostMapping("/scrape")
    public Recipe scrapeRecipe(@RequestBody String URL)
            throws WrongTokenTypeException, RecipeAlreadyExistsException, IOException {
        Recipe scrapedRecipe = recipeService.getRecipeMetaData(URL);
        return recipeService.saveRecipe(scrapedRecipe);
    }

    @PostMapping("/filter")
    public Set<Recipe> filterRecipesByIngredients(@RequestBody IngredientsWrapper ingredientsWrapper) {
        return recipeService.getRecipeByConditions(ingredientsWrapper.getIngredientNames());
    }
}

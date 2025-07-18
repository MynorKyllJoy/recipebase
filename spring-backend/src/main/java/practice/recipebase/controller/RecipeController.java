package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    IngredientService ingredientService;

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        return new ResponseEntity<>(allRecipes, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredients();
        return new ResponseEntity<>(allIngredients, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<Recipe> uploadRecipe(@RequestBody UploadedRecipeWrapper recipeWrapper)
            throws WrongTokenTypeException {
        Recipe uploadedRecipe = recipeService.createUploadedRecipe(recipeWrapper);
        Recipe savedRecipe = recipeService.saveRecipe(uploadedRecipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @PostMapping("/scrape")
    public ResponseEntity<Recipe> scrapeRecipe(@RequestBody String URL)
            throws WrongTokenTypeException, RecipeAlreadyExistsException, IOException {
        Recipe scrapedRecipe = recipeService.getRecipeMetaData(URL);
        Recipe savedRecipe = recipeService.saveRecipe(scrapedRecipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<Set<Recipe>> filterRecipesByIngredients(@RequestBody IngredientsWrapper ingredientsWrapper) {
        Set<Recipe> filteredRecipes = recipeService.getRecipeByConditions(ingredientsWrapper.getIngredientNames());
        return new ResponseEntity<>(filteredRecipes, HttpStatus.OK);
    }
}

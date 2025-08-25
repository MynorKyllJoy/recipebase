package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import practice.recipebase.exceptions.RecipeAlreadyExistsException;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.wrappers.IngredientsWrapper;
import practice.recipebase.wrappers.RecipeSiteWrapper;
import practice.recipebase.wrappers.UploadedRecipeWrapper;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;
import practice.recipebase.service.IngredientService;
import practice.recipebase.service.JWTService;
import practice.recipebase.service.RecipeService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    JWTService jwtService;

    @GetMapping("/all")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/id/{id}")
    public Recipe getRecipe(@PathVariable("id") String id) {
        return recipeService.getRecipeById(id);
    }

    @GetMapping("/ingredients")
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @PostMapping("/upload")
    public Recipe uploadRecipe(@RequestHeader HttpHeaders httpHeaders, @RequestBody UploadedRecipeWrapper recipeWrapper)
            throws WrongTokenTypeException {
        // TODO: Move logic out of controller
        List<String> headers = httpHeaders.get("Authorization");
        if(headers.isEmpty()) {
            // TODO: Error handling
        }
        String jwtToken = headers.getFirst().substring(7);
        String username = jwtService.extractUsername(jwtToken);

        Recipe uploadedRecipe = recipeService.createUploadedRecipe(recipeWrapper);
        uploadedRecipe.setSource(username);
        return recipeService.saveRecipe(uploadedRecipe);
    }

    @PostMapping("/scrape")
    public Recipe scrapeRecipe(@RequestBody RecipeSiteWrapper recipeSite)
            throws WrongTokenTypeException, RecipeAlreadyExistsException, IOException {
        Recipe scrapedRecipe = recipeService.getRecipeMetaData(recipeSite.getRecipeSite());
        return recipeService.saveRecipe(scrapedRecipe);
    }

    @PostMapping("/filter")
    public Set<Recipe> filterRecipesByIngredients(@RequestBody IngredientsWrapper ingredientsWrapper) {
        return recipeService.getRecipeByConditions(ingredientsWrapper.getIngredientNames());
    }
}

package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.misc.UploadedRecipeWrapper;
import practice.recipebase.model.Recipe;
import practice.recipebase.service.RecipeService;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @PostMapping("/upload")
    public ResponseEntity<Recipe> uploadRecipe(@RequestBody UploadedRecipeWrapper recipeWrapper)
            throws WrongTokenTypeException {
        Recipe uploadedRecipe = recipeService.createUploadedRecipe(recipeWrapper);
        Recipe savedRecipe = recipeService.saveRecipe(uploadedRecipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }
}

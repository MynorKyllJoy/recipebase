package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import practice.recipebase.exceptions.RecipeAlreadyExistsException;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.misc.SelectedIngredientWrapper;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;
import practice.recipebase.service.IngredientService;
import practice.recipebase.service.RecipeService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
public class DebugController {
    @Autowired
    RecipeService recipeService;

    @Autowired
    IngredientService ingredientService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/listRecipe")
    public String listAllRecipes(Model model) {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        model.addAttribute("recipes", allRecipes);
        return "listAllRecipes";
    }

    @GetMapping("/scrapeRecipe")
    public String scrapeRecipe() {
        return "scrapeRecipe";
    }

    @PostMapping("/scrapeRecipe")
    public String showScrapedRecipe(@RequestParam("recipeSiteURL") String URL, Model model, RedirectAttributes redirectAttributes) {
        try {
            Recipe scrapedRecipe = recipeService.getRecipeMetaData(URL);
            Recipe recipe = recipeService.saveRecipe(scrapedRecipe);
            redirectAttributes.addAttribute("id", recipe.getId());
            return "redirect:/showRecipe";
        } catch (IOException | WrongTokenTypeException | RecipeAlreadyExistsException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/showRecipe")
    public String showRecipe(@RequestParam(name="id") String recipeId, Model model) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        model.addAttribute("recipe", recipe);
        return "showRecipe";
    }

    @GetMapping("/searchRecipe")
    public String searchRecipe(Model model) {
        List<Ingredient> allIngredients = ingredientService.getAllIngredients();
        model.addAttribute("selectedIngredientWrapper", new SelectedIngredientWrapper());
        model.addAttribute("allIngredients", allIngredients);
        return "searchRecipe";
    }

    @PostMapping("/showFilteredRecipes")
    public String showFilteredRecipes(@ModelAttribute("selectedIngredientWrapper") SelectedIngredientWrapper wrapper, Model model) {
        Set<Recipe> filteredRecipes = recipeService.getRecipeByConditions(wrapper.getSelectedIngredients());
        model.addAttribute("recipes", filteredRecipes);
        return "listAllRecipes";
    }
}

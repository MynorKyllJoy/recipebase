package practice.recipebase.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.model.Recipe;
import practice.recipebase.service.RecipeService;

import java.io.IOException;

@Controller
public class MainController {
    @Autowired
    RecipeService recipeService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/scrapeRecipe")
    public String scrapeRecipe() {
        return "scrapeRecipe";
    }

    @PostMapping("/scrapeRecipe")
    public String showScrapedRecipe(@RequestParam("recipeSiteURL") String URL, Model model) {
        try {
            Recipe scrapedRecipe = recipeService.getRecipeMetaData(URL);
            recipeService.save(scrapedRecipe);
            model.addAttribute("recipe", scrapedRecipe);
            return "showScrapedRecipe";
        } catch (IOException | WrongTokenTypeException ex) {
            System.out.println(ex);
            return "index";
        }
    }
}

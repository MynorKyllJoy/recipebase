package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.recipebase.service.RecipeService;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hello, World!", HttpStatus.OK);
    }
}

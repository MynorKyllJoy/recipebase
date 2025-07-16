package practice.recipebase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.recipebase.model.Ingredient;
import practice.recipebase.repository.IngredientRepository;

import java.util.List;


@Service
public class IngredientService {
    @Autowired
    IngredientRepository ingredientRepo;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepo.findAll();
    }
}

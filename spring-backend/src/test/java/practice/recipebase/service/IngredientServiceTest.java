package practice.recipebase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.recipebase.model.Ingredient;
import practice.recipebase.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @Mock
    IngredientRepository ingredientRepo;

    @InjectMocks
    IngredientService ingredientService;

    @Test
    public void testGetAllIngredients() {
        Ingredient onion = new Ingredient("onion");
        Ingredient garlic = new Ingredient("garlic");
        Ingredient shallot = new Ingredient("shallot");

        given(ingredientRepo.findAll())
                .willReturn(List.of(onion, garlic, shallot));

        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        assertThat(ingredients).isNotNull();
        assertThat(ingredients.size()).isEqualTo(3);
        assertThat(ingredients.get(0).getName()).isEqualTo("onion");
        assertThat(ingredients.get(1).getName()).isEqualTo("garlic");
        assertThat(ingredients.get(2).getName()).isEqualTo("shallot");
    }

    @Test
    public void testGetAllIngredientsEmptyList() {

        given(ingredientRepo.findAll())
                .willReturn(new ArrayList<>());

        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        assertThat(ingredients).isNotNull();
        assertThat(ingredients.isEmpty()).isTrue();
    }
}

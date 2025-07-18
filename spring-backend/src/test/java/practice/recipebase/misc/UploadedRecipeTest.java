package practice.recipebase.misc;

import org.junit.jupiter.api.Test;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;
import practice.recipebase.model.Requirement;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UploadedRecipeTest {
    private Recipe getResultRecipe() {
        Requirement kimchi = new Requirement(Set.of("chopped"), "cup", 1d, new Ingredient("kimchi"));
        Requirement flour = new Requirement(new HashSet<>(), "cup", 0.5d, new Ingredient("flour"));
        Requirement water = new Requirement(new HashSet<>(), "cup", 0.25d, new Ingredient("water"));

        return new Recipe(
            "Kimchi Pancake",
            "Savory Kimchi Pancakes",
            "xxx",
            List.of(kimchi, flour, water),
            List.of(
                    "Mix everything into a smooth batter.",
                    "Add oil to the pan. Heat pan up over medium-high heat. Add batter to the pan.",
                    "Flip once bubbles start to form on the surface. Then fry for 5-8 mins on second side.",
                    "Repeat until no more batter is left. Serve."
            ),
            List.of(
                    "1 cup chopped Kimchi",
                    "1/2 cup flour",
                    "1/4 cup water"
            )
        );
    }


    @Test
    public void testUploadedRecipe() throws IOException, WrongTokenTypeException {
        Recipe expectedRecipe = this.getResultRecipe();

        UploadedRecipeWrapper wrapper = new UploadedRecipeWrapper(
                expectedRecipe.getTitle(),
                expectedRecipe.getDescription(),
                expectedRecipe.getSource(),
                expectedRecipe.getIngredientInfos(),
                expectedRecipe.getInstructions()
        );

        UploadedRecipe uploadedRecipe = new UploadedRecipe(wrapper);

        assertThat(uploadedRecipe.getTitle()).isEqualTo(expectedRecipe.getTitle());
        assertThat(uploadedRecipe.getDescription()).isEqualTo(expectedRecipe.getDescription());
        assertThat(uploadedRecipe.getSource()).isEqualTo(expectedRecipe.getSource());
        assertThat(uploadedRecipe.getInstructions()).isEqualTo(expectedRecipe.getInstructions());
        assertThat(uploadedRecipe.getIngredientInfos()).isEqualTo(expectedRecipe.getIngredientInfos());
        assertThat(uploadedRecipe.createRecipe()).isEqualTo(expectedRecipe);
    }
}

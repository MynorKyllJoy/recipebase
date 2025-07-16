package practice.recipebase.service;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.recipebase.exceptions.RecipeAlreadyExistsException;
import practice.recipebase.misc.RecipeSiteRequestAdapter;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;
import practice.recipebase.model.Requirement;
import practice.recipebase.repository.RecipeRepository;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    RecipeRepository recipeRepo;

    @InjectMocks
    RecipeService recipeService;

    private final String fakeURL = "https://github.com/MynorKyllJoy/recipebase";

    private Recipe getResultRecipe() {
        Requirement eggs = new Requirement(new HashSet<>(), null, 2d, new Ingredient("eggs"));
        Requirement flour = new Requirement(new HashSet<>(), "g", 200d, new Ingredient("flour"));
        Requirement sugar = new Requirement(new HashSet<>(), "tbsp", 2d, new Ingredient("sugar"));
        Requirement milk = new Requirement(new HashSet<>(), "ml", 200d, new Ingredient("milk"));
        return new Recipe(
                "Pancakes",
                "A very simple pancake recipe.",
                this.fakeURL,
                List.of(eggs, flour, sugar, milk),
                List.of(
                        "Add all ingredients into a bowl and mix until everything is combined. Don't overmix.",
                        "Add oil into the frying pan. Heat up the oil over Medium high heat.",
                        "Add enough pancake dough to cover the frying pan. Fry until bubbles form on top. Then flip the pancakes and fry for 1 or 2 more mins. Repeat until there is no more pancake dough.",
                        "Serve the pancakes."
                ),
                List.of(
                        "2 eggs",
                        "200g flour",
                        "2 tbsp sugar",
                        "200 ml milk"
                )
        );
    }

    @Test
    void testSaveRecipe() {
        Recipe expectedRecipe = this.getResultRecipe();
        given(recipeRepo.save(expectedRecipe)).willReturn(this.getResultRecipe());
        Recipe actualRecipe = recipeService.saveRecipe(expectedRecipe);
        assertThat(actualRecipe).isEqualTo(expectedRecipe);
    }

    @Test
    void testGetAllRecipes() {
        given(recipeRepo.findAll()).willReturn(List.of(this.getResultRecipe()));

        List<Recipe> recipes = recipeService.getAllRecipes();
        assertThat(recipes.size()).isEqualTo(1);
        assertThat(recipes.getFirst()).isEqualTo(this.getResultRecipe());
    }

    @Test
    void testGetAllRecipesEmpty() {
        given(recipeRepo.findAll()).willReturn(new ArrayList<>());

        List<Recipe> emptyList = recipeService.getAllRecipes();
        assertThat(emptyList.isEmpty()).isTrue();
    }

    @Test
    void testGetRecipeById() {
        Recipe expectedRecipe = this.getResultRecipe();
        given(recipeRepo.findById(anyString())).willReturn(Optional.of(this.getResultRecipe()));
        Recipe actualRecipe = recipeService.getRecipeById("1");

        assertThat(actualRecipe).isEqualTo(expectedRecipe);
    }

    @Test
    void testGetRecipeByIdNotFound() {
        given(recipeRepo.findById(anyString())).willReturn(Optional.empty());
        Recipe actualRecipe = recipeService.getRecipeById("1");

        assertThat(actualRecipe).isNull();
    }

    @Test
    void testGetRecipeBySource() {
        given(recipeRepo.findRecipeBySource(this.fakeURL)).willReturn(List.of(this.getResultRecipe()));
        List<Recipe> actualRecipe = recipeService.getRecipeBySource(this.fakeURL);
        Recipe expectedRecipe = this.getResultRecipe();

        assertThat(actualRecipe.size()).isEqualTo(1);
        assertThat(actualRecipe.getFirst()).isEqualTo(expectedRecipe);
    }

    @Test
    void testGetRecipeByConditions() {
        given(recipeRepo.findRecipeByIngredientRequirements(List.of("eggs")))
                .willReturn(List.of(this.getResultRecipe()));
        Set<Recipe> actualRecipes = recipeService.getRecipeByConditions(List.of("eggs"));
        Recipe expectedRecipe = this.getResultRecipe();

        assertThat(actualRecipes.size()).isEqualTo(1);
        assertThat(actualRecipes.iterator().next()).isEqualTo(expectedRecipe);
    }


    // below are test mocking the RecipeSiteRequestAdapter
    @Test
    void getScrapedRecipeTestMockRequest() throws Exception {
        Recipe expectedRecipe = this.getResultRecipe();

        // writing the html as a string inside the test class would decrease readability
        // instead the html is an actual resource file which is loaded and read
        ClassLoader loader = this.getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(loader.getResource("requestMock.html")).getFile());
        assertThat(file.exists()).isTrue();

        String html = Files.readString(file.toPath());

        given(recipeRepo.findRecipeBySource(this.fakeURL))
                .willReturn(new ArrayList<>());

        // mock adapter
        try (MockedStatic<RecipeSiteRequestAdapter> adapter = Mockito.mockStatic(RecipeSiteRequestAdapter.class)) {
            adapter.when(
                    () -> RecipeSiteRequestAdapter.getRecipeSite(this.fakeURL))
                    .thenReturn(Jsoup.parse(html));

            Recipe actualRecipe = recipeService.getRecipeMetaData(this.fakeURL);
            assertThat(actualRecipe).isEqualTo(expectedRecipe);
        }
    }

    @Test
    void getScrapedRecipeTestMockRequestGraph() throws Exception {
        Recipe expectedRecipe = this.getResultRecipe();

        // writing the html as a string inside the test class would decrease readability
        // instead the html is an actual resource file which is loaded and read
        ClassLoader loader = this.getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(loader.getResource("requestMockGraph.html")).getFile());
        assertThat(file.exists()).isTrue();

        String html = Files.readString(file.toPath());

        given(recipeRepo.findRecipeBySource(this.fakeURL))
                .willReturn(new ArrayList<>());

        // mock adapter
        try (MockedStatic<RecipeSiteRequestAdapter> adapter = Mockito.mockStatic(RecipeSiteRequestAdapter.class)) {
            adapter.when(
                            () -> RecipeSiteRequestAdapter.getRecipeSite(this.fakeURL))
                    .thenReturn(Jsoup.parse(html));

            Recipe actualRecipe = recipeService.getRecipeMetaData(this.fakeURL);
            assertThat(actualRecipe).isEqualTo(expectedRecipe);
        }
    }

    @Test
    void getScrapedRecipeTestMockRequestMultiJsonLd() throws Exception {
        Recipe expectedRecipe = this.getResultRecipe();

        // writing the html as a string inside the test class would decrease readability
        // instead the html is an actual resource file which is loaded and read
        ClassLoader loader = this.getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(loader.getResource("requestMockMultiJsonLd.html")).getFile());
        assertThat(file.exists()).isTrue();

        String html = Files.readString(file.toPath());

        given(recipeRepo.findRecipeBySource(this.fakeURL))
                .willReturn(new ArrayList<>());

        // mock adapter
        try (MockedStatic<RecipeSiteRequestAdapter> adapter = Mockito.mockStatic(RecipeSiteRequestAdapter.class)) {
            adapter.when(
                            () -> RecipeSiteRequestAdapter.getRecipeSite(this.fakeURL))
                    .thenReturn(Jsoup.parse(html));

            Recipe actualRecipe = recipeService.getRecipeMetaData(this.fakeURL);
            assertThat(actualRecipe).isEqualTo(expectedRecipe);
        }
    }

    @Test
    public void getScrapedRecipeException() {
        given(recipeRepo.findRecipeBySource(this.fakeURL))
                .willReturn(List.of(this.getResultRecipe()));

        Exception ex = assertThrows(
                RecipeAlreadyExistsException.class,
                () -> recipeService.getRecipeMetaData(this.fakeURL)
        );

        String actualMessage = ex.getMessage();
        String expectedMessage = "The recipe you are trying to scrape is already in the database.";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}

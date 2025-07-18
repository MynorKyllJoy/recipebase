package practice.recipebase.misc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Recipe;
import practice.recipebase.model.Requirement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ScrapedRecipeTest {
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
                        "Add enough pancake dough to cover the frying pan. Fry until bubbles form on top. "
                                + "Then flip the pancakes and fry for 1 or 2 more mins. "
                                + "Repeat until there is no more pancake dough.",
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
    public void testScrapedRecipeFromMockRequest() throws IOException, WrongTokenTypeException {
        // create Document doc from testResource
        ClassLoader loader = this.getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(loader.getResource("requestMock.html")).getFile());
        assertThat(file.exists()).isTrue();
        Document doc = Jsoup.parse(Files.readString(file.toPath()));

        ScrapedRecipe scrapedRecipe = new ScrapedRecipe(doc, this.fakeURL);
        Recipe resultingRecipe = this.getResultRecipe();

        assertThat(scrapedRecipe.getTitle()).isEqualTo(resultingRecipe.getTitle());
        assertThat(scrapedRecipe.getDescription()).isEqualTo(resultingRecipe.getDescription());
        assertThat(scrapedRecipe.getSource()).isEqualTo(this.fakeURL);
        assertThat(scrapedRecipe.getInstructions()).isEqualTo(resultingRecipe.getInstructions());
        assertThat(scrapedRecipe.getIngredientInfos()).isEqualTo(resultingRecipe.getIngredientInfos());
        assertThat(scrapedRecipe.createRecipe()).isEqualTo(resultingRecipe);
    }

    @Test
    public void testScrapedRecipeFromMockRequestGraph() throws IOException, WrongTokenTypeException {
        // create Document doc from testResource
        ClassLoader loader = this.getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(loader.getResource("requestMockGraph.html")).getFile());
        assertThat(file.exists()).isTrue();
        Document doc = Jsoup.parse(Files.readString(file.toPath()));

        ScrapedRecipe scrapedRecipe = new ScrapedRecipe(doc, this.fakeURL);
        Recipe resultingRecipe = this.getResultRecipe();

        assertThat(scrapedRecipe.getTitle()).isEqualTo(resultingRecipe.getTitle());
        assertThat(scrapedRecipe.getDescription()).isEqualTo(resultingRecipe.getDescription());
        assertThat(scrapedRecipe.getSource()).isEqualTo(this.fakeURL);
        assertThat(scrapedRecipe.getInstructions()).isEqualTo(resultingRecipe.getInstructions());
        assertThat(scrapedRecipe.getIngredientInfos()).isEqualTo(resultingRecipe.getIngredientInfos());
        assertThat(scrapedRecipe.createRecipe()).isEqualTo(resultingRecipe);
    }

    @Test
    public void testScrapedRecipeFromMockRequestMultiJsonLd() throws IOException, WrongTokenTypeException {
        // create Document doc from testResource
        ClassLoader loader = this.getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(loader.getResource("requestMockMultiJsonLd.html")).getFile());
        assertThat(file.exists()).isTrue();
        Document doc = Jsoup.parse(Files.readString(file.toPath()));

        ScrapedRecipe scrapedRecipe = new ScrapedRecipe(doc, this.fakeURL);
        Recipe resultingRecipe = this.getResultRecipe();

        assertThat(scrapedRecipe.getTitle()).isEqualTo(resultingRecipe.getTitle());
        assertThat(scrapedRecipe.getDescription()).isEqualTo(resultingRecipe.getDescription());
        assertThat(scrapedRecipe.getSource()).isEqualTo(this.fakeURL);
        assertThat(scrapedRecipe.getInstructions()).isEqualTo(resultingRecipe.getInstructions());
        assertThat(scrapedRecipe.getIngredientInfos()).isEqualTo(resultingRecipe.getIngredientInfos());
        assertThat(scrapedRecipe.createRecipe()).isEqualTo(resultingRecipe);
    }
}

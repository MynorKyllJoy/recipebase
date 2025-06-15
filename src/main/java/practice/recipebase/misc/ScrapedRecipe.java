package practice.recipebase.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import practice.recipebase.model.Ingredient;

import java.io.IOException;
import java.util.List;

public class ScrapedRecipe extends RecipeTemplate {
    JsonNode recipeNode;
    String siteURL;

    public ScrapedRecipe(Document recipeSite, String siteURL) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // works only for sites with one ld+json at the moment
        Elements metadata = recipeSite.getElementsByAttributeValue("type", "application/ld+json");
        String metadataString = metadata.getFirst().data();
        JsonNode metadataNode = mapper.readTree(metadataString);
        this.recipeNode = this.findRecipeNode(metadataNode);
        // the URL is sometimes missing in the ld+json
        this.siteURL = siteURL;
    }

    private JsonNode findRecipeNode(JsonNode metadataNode) {
        /*
         Every recipe site metadata schema can be slightly different.
         Usually, the recipe data can in the most outer json object or within a json array, e.g.:
         {"@type": "Recipe", ...} or {"@graph": [{...}, {"@type": "Recipe"}, {...}]}
        */
        JsonNode typeNode = metadataNode.get("@type");
        JsonNode graphNode = metadataNode.get("@graph");

        if(typeNode != null && typeNode.asText().equals("Recipe")) {
            return metadataNode;
        } else if(graphNode != null) {
            int index = 0;
            while(index < graphNode.size()) {
                JsonNode currNode = graphNode.get(index);
                if(currNode.get("@type").asText().equals("Recipe")) {
                    return currNode;
                }
                index += 1;
            }
        }
        return null; // EX: change later to throw exception
    }

    @Override
    String getTitle() {
        return recipeNode.get("name").asText();
    }

    @Override
    String getDescription() {
        return recipeNode.get("description").asText();
    }

    @Override
    String getSource() {
        return this.siteURL;
    }

    @Override
    List<Ingredient> getIngredients() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode ingredientNode = this.recipeNode.get("recipeIngredient");
        TypeReference<List<String>> typeRef = new TypeReference<>() {};
        List<String> ingredientStrings = mapper.readValue(ingredientNode.traverse(), typeRef);
        return ingredientStrings.stream().map(Ingredient::new).toList();
    }

    @Override
    List<String> getInstructions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode instructionNode = recipeNode.get("recipeInstructions");
        TypeReference<List<RecipeInstructionWrapper>> typeRef = new TypeReference<>() {};
        List<RecipeInstructionWrapper> wrappedInstructions = mapper.readValue(instructionNode.traverse(), typeRef);
        return  wrappedInstructions.stream().map(RecipeInstructionWrapper::getText).toList();
    }
}

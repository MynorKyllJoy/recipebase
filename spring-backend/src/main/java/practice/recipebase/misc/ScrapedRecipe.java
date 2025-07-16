package practice.recipebase.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrapedRecipe extends RecipeTemplate {
    JsonNode recipeNode;
    String siteURL;

    public ScrapedRecipe(Document recipeSite, String siteURL) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Elements metadataList = recipeSite.getElementsByAttributeValue("type", "application/ld+json");

        // a website can have multiple ld+json scripts, find the one that has the recipe data
        for(Element metadata : metadataList.stream().toList()) {
            String metadataString = metadata.data();
            JsonNode metadataNode = mapper.readTree(metadataString);
            JsonNode currNode = this.findRecipeNode(metadataNode);
            if(currNode != null) {
                this.recipeNode = currNode;
                break;
            }
        }
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
        return null;
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
    List<String> getIngredientInfos() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode ingredientNode = this.recipeNode.get("recipeIngredient");
        TypeReference<List<String>> typeRef = new TypeReference<>() {};
        return mapper.readValue(ingredientNode.traverse(), typeRef);
    }

    @Override
    List<String> getInstructions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode instructionNode = recipeNode.get("recipeInstructions");

        // Instructions are mostly JSON arrays, but they are sometimes Strings
        // Possible EX: Test for empty instructions
        try {
            TypeReference<List<RecipeInstructionWrapper>> typeRef = new TypeReference<>() {};
            List<RecipeInstructionWrapper> wrappedInstructions = mapper.readValue(instructionNode.traverse(), typeRef);
            return wrappedInstructions.stream().map(RecipeInstructionWrapper::getText).toList();
        } catch (IOException ex) {
            List<String> list = new ArrayList<>();
            list.add(instructionNode.asText());
            return list;
        }
    }
}

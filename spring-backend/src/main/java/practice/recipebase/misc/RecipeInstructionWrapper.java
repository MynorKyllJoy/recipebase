package practice.recipebase.misc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class RecipeInstructionWrapper {
    private String name;
    private String text;
}

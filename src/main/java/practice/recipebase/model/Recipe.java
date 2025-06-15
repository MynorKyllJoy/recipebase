package practice.recipebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Recipe {
    @Id @GeneratedValue
    private String id;
    private String title;
    private String description;
    private String source;
    private List<Ingredient> ingredients;
    private List<String> instructions;

    public Recipe(String title, String description, String source,
                  List<Ingredient> ingredients, List<String> instructions) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
}

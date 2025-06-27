package practice.recipebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private List<String> instructions;
    @Relationship(type="REQUIREMENT", direction=Relationship.Direction.OUTGOING)
    private List<Requirement> requiredIngredients;

    public Recipe(String title, String description, String source,
                  List<Requirement> requiredIngredients, List<String> instructions) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.instructions = instructions;
        this.requiredIngredients = requiredIngredients;
    }

    public Set<String> getIngredientNames() {
        return requiredIngredients.stream().map(i -> i.getIngredient().getName()).collect(Collectors.toSet());
    }
}

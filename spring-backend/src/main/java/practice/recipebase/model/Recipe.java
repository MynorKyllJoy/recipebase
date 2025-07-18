package practice.recipebase.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
@Node
public class Recipe {
    @Id @GeneratedValue
    private String id;
    private String title;
    private String description;
    private String source;
    private List<String> instructions;
    private List<String> ingredientInfos;
    @Relationship(type="REQUIREMENT", direction=Relationship.Direction.OUTGOING)
    private List<Requirement> requirements;

    public Recipe(String title, String description, String source,
                  List<Requirement> requirements, List<String> instructions, List<String> ingredientInfos) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.instructions = instructions;
        this.requirements = requirements;
        this.ingredientInfos = ingredientInfos;
    }
}

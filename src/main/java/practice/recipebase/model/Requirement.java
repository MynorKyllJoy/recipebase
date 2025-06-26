package practice.recipebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class Requirement {
    @Id @GeneratedValue
    private String id;
    private Set<String> states;
    private String unit;
    private Float amount;
    @TargetNode
    Ingredient ingredient;

}

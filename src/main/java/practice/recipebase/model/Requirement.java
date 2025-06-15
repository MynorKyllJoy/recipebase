package practice.recipebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class Requirement {
    @Id @GeneratedValue
    private Long id;
    private float amount;
    private String state;
    @TargetNode
    Ingredient ingredient;
}

package practice.recipebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Node
public class Ingredient {
    @Id
    private String name;
}

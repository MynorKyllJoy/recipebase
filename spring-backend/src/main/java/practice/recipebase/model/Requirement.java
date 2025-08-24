package practice.recipebase.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Set;

@Data
@EqualsAndHashCode
@RelationshipProperties
public class Requirement {
    @Id
    @GeneratedValue
    private String id;
    private Set<String> states;
    private String unit;
    private Double amount;
    @TargetNode
    Ingredient ingredient;

    public Requirement(Set<String> states, String unit, Double amount, Ingredient ingredient) {
        this.states = states;
        this.unit = unit;
        this.amount = amount;
        this.ingredient = ingredient;
    }
}

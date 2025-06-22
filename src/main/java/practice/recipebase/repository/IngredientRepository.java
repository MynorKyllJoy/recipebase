package practice.recipebase.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import practice.recipebase.model.Ingredient;


public interface IngredientRepository  extends Neo4jRepository<Ingredient, String> {
}

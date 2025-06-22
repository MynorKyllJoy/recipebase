package practice.recipebase.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import practice.recipebase.model.Recipe;

public interface RecipeRepository extends Neo4jRepository<Recipe, String> {
}

package practice.recipebase.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import practice.recipebase.model.Recipe;

import java.util.List;

public interface RecipeRepository extends Neo4jRepository<Recipe, String> {
    @Query("MATCH ((r:Recipe) WHERE r.source=$source) RETURN r")
    List<Recipe> findRecipeBySource(@Param("source") String source);

    @Query(
            "WITH $ingredientRequirements AS ingredientNames " +
            "UNWIND ingredientNames AS ingredientName " +
            "MATCH (r:Recipe)-[req:REQUIREMENT]->(i:Ingredient) WHERE i.name=ingredientName " +
            "RETURN r")
    List<Recipe> findRecipeByIngredientRequirements(@Param("ingredientRequirements") List<String> ingredientRequirements);
}

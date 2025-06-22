package practice.recipebase.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import practice.recipebase.model.Recipe;

import java.util.List;

public interface RecipeRepository extends Neo4jRepository<Recipe, String> {
    @Query(value="MATCH ((r:Recipe) WHERE r.source=$source) RETURN r")
    List<Recipe> findRecipeBySource(@Param("source") String source);
}

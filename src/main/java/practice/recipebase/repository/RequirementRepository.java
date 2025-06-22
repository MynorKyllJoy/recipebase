package practice.recipebase.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import practice.recipebase.model.Requirement;

public interface RequirementRepository extends Neo4jRepository<Requirement, Long> {
}

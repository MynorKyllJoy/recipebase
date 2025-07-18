package practice.recipebase.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import practice.recipebase.model.AppUser;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);
}

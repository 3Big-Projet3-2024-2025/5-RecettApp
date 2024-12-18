package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.RecipeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for RecipeType.
 * Provides methods for database operations.
 */
@Repository
public interface RecipeTypeRepository extends JpaRepository<RecipeType, Integer> {
}

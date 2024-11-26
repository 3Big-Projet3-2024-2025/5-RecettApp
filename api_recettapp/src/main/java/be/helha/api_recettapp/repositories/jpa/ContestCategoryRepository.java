package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.ContestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ContestCategory.
 * It provides methods to interact with the database.
 * Extends JpaRepository to use built-in CRUD operations.
 */
@Repository
public interface ContestCategoryRepository extends JpaRepository<ContestCategory, Long> {
}

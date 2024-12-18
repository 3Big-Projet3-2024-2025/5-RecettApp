package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Evaluation} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations.
 */
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    /**
     * Finds all evaluations associated with a specific entry ID.
     *
     * @param entryId The ID of the entry.
     * @return A list of evaluations linked to the given entry ID.
     */
    List<Evaluation> findByEntryId(Long entryId);


    /**
     * Finds all evaluations associated with a specific recipe ID.
     *
     * @param recipeId The ID of the recipe.
     * @return A list of evaluations linked to the given recipe ID.
     */
    List<Evaluation> findByRecipeId(Long recipeId);
}

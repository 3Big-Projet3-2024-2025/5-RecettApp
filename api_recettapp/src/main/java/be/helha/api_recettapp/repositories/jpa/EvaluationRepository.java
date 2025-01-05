package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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

    /**
     *
     *  Checks whether an evaluation exists for the user (via Entry) and the same recipe
     * in the time range.
     *
     * @param userId the id of the user
     * @param recipeId the id of the user
     * @param start the start date of the evaluation
     * @param end the start date of the evaluation
     * @return boolean
     */
    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END
        FROM Evaluation e
        WHERE e.entry.users.id = :userId
          AND e.recipe.id = :recipeId
          AND e.dateEvaluation BETWEEN :start AND :end
    """)
    boolean existsByUserAndRecipeAndDateBetween(
            @org.springframework.data.repository.query.Param("userId") Long userId,
            @org.springframework.data.repository.query.Param("recipeId") Long recipeId,
            @org.springframework.data.repository.query.Param("start") LocalDateTime start,
            @org.springframework.data.repository.query.Param("end") LocalDateTime end
    );
}

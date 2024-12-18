package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;

import java.util.List;

/**
 * Service interface for managing evaluations.
 * Provides methods to add, delete, and retrieve evaluations.
 */
public interface IEvaluationService {
    /**
     * Adds a new review.
     *
     * @param evaluation The evaluation to add.
     ** @return The evaluation added.
     */
    Evaluation addEvaluation(Evaluation evaluation);

    /**
     * Deletes a review by its ID.
     *
     * @param id The ID of the assessment to delete.
     * @param isAdmin Indique si l'utilisateur est administrateur.
     */
    void deleteEvaluation(Long id, boolean isAdmin);

    /**
     * Retrieves all ratings.
     *
     * @return A list of evaluations.
     */
    List<Evaluation> getAllEvaluations();

    /**
     * Retrieves all evaluations linked to a specific entry.
     *
     * @param entryId The ID of the entry for which evaluations are to be retrieved.
     * @return A list of evaluations associated with the specified entry ID.
     */
    List<Evaluation> getEvaluationsByEntry(Long entryId);

    /**
     * Retrieves all evaluations linked to a specific recipe.
     *
     * @param recipeId The ID of the recipe for which evaluations are to be retrieved.
     * @return A list of evaluations associated with the specified recipe ID.
     */
    List<Evaluation> getEvaluationsByRecipe(Long recipeId);

}

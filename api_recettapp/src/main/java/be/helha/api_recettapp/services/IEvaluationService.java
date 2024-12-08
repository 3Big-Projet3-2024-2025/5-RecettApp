package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;

import java.util.List;

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
     */
    void deleteEvaluation(Long id);

    /**
     * Retrieves all ratings.
     *
     * @return A list of evaluations.
     */
    List<Evaluation> getAllEvaluations();
}

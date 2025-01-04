package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.services.IEvaluationService;
import be.helha.api_recettapp.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * REST controller for managing evaluations in the application.
 * Provides endpoints to add, delete, and retrieve evaluations.
 */
@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    /**
     * Service layer for handling evaluation-related business logic.
     */
    @Autowired
    private IEvaluationService evaluationService;

    /**
     * Adds a new evaluation to the system.
     *
     * @param evaluation The evaluation to add, passed in the request body.
     * @return The added evaluation wrapped in a {@link ResponseEntity}.
     */
    @PostMapping
    public ResponseEntity<?> addEvaluation(@RequestBody Evaluation evaluation) {
        try {
            evaluation.setId(0L);
            Evaluation savedEval = evaluationService.addEvaluation(evaluation);
            return ResponseEntity.ok(savedEval);
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    /**
     * Deletes an evaluation by its ID.
     * Only administrators are allowed to perform this action.
     *
     * @param id      The ID of the evaluation to delete, passed as a path variable.
     * @param isAdmin Boolean flag indicating if the user is an administrator, passed as a query parameter.
     * @return An empty {@link ResponseEntity} with no content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id, @RequestParam boolean isAdmin) {
        evaluationService.deleteEvaluation(id, isAdmin);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all evaluations from the system.
     *
     * @return A list of all evaluations wrapped in a {@link ResponseEntity}.
     */
    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    /**
     * Retrieves all evaluations linked to a specific entry.
     *
     * @param entryId The ID of the entry.
     * @return A list of evaluations for the entry.
     */
    @GetMapping("/entry/{entryId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByEntry(@PathVariable Long entryId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByEntry(entryId));
    }

    /**
     * Retrieves all evaluations linked to a specific recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return A list of evaluations for the recipe.
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByRecipe(@PathVariable Long recipeId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByRecipe(recipeId));
    }

}

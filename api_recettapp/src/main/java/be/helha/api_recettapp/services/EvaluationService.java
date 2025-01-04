package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class responsible for managing evaluations.
 * Implements the {@link IEvaluationService} interface to provide the core logic for adding, deleting,
 * and retrieving evaluations.
 */
@Service
public class EvaluationService implements IEvaluationService{

    @Autowired
    private EvaluationRepository evaluationRepository;

    /**
     * Adds a new evaluation to the system.
     *
     * @param evaluation The evaluation entity to save.
     * @return The saved evaluation.
     */
    @Override
    public Evaluation addEvaluation(Evaluation evaluation) {
        Long userId = evaluation.getEntry().getUsers().getId();
        Long recipeId = (long) evaluation.getRecipe().getId();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        boolean alreadyRatedToday = evaluationRepository.existsByUserAndRecipeAndDateBetween(
                userId,
                recipeId,
                startOfDay,
                endOfDay
        );

        if (alreadyRatedToday) {
            throw new IllegalStateException("You've already evaluated this recipe today !");
        }

        evaluation.setDateEvaluation(LocalDateTime.now());
        return evaluationRepository.save(evaluation);
    }


    /**
     * Deletes an evaluation by its ID.
     * This action is restricted to administrators.
     *
     * @param id      The ID of the evaluation to delete.
     * @param isAdmin A flag indicating whether the user is an administrator.
     * @throws SecurityException if the user is not an administrator.
     */
    @Override
    public void deleteEvaluation(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Only an administrator can delete a review.");
        }
        evaluationRepository.deleteById(id);
    }


    /**
     * Retrieves all evaluations in the system.
     *
     * @return A list of all evaluations.
     */
    @Override
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    /**
     * Retrieves all evaluations linked to a specific entry.
     *
     * @param entryId The ID of the entry.
     * @return A list of evaluations for the specified entry.
     */
    public List<Evaluation> getEvaluationsByEntry(Long entryId) {
        return evaluationRepository.findByEntryId(entryId);
    }

    /**
     * Retrieves all evaluations linked to a specific recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return A list of evaluations for the specified recipe.
     */
    public List<Evaluation> getEvaluationsByRecipe(Long recipeId) {
        return evaluationRepository.findByRecipeId(recipeId);
    }


}

package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * Adds a new evaluation to the database.
     *
     * @param evaluation the evaluation to be added
     * @return the added evaluation
     */
    @Override
    public Evaluation addEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }


    /**
     * Deletes an evaluation by its ID.
     * Only administrators are allowed to delete evaluations.
     *
     * @param id      the ID of the evaluation to delete
     * @param isAdmin a flag indicating whether the user is an administrator
     * @throws SecurityException if the user is not an administrator
     */
    @Override
    public void deleteEvaluation(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Only an administrator can delete a review.");
        }
        evaluationRepository.deleteById(id);
    }

    /**
     * Retrieves all evaluations from the database.
     *
     * @return a list of all evaluations
     */
    @Override
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    /**
     * Saves a user to the database.
     * Can be used to create a new user or update an existing user.
     * @param evaluation the user to save
     * @return the saved user
     */
    public Evaluation save(Evaluation evaluation) {

        return evaluationRepository.save(evaluation);
    }

}

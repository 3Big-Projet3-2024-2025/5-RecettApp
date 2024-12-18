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

    @Override
    public Evaluation addEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @Override
    public void deleteEvaluation(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Only an administrator can delete a review.");
        }
        evaluationRepository.deleteById(id);
    }

    @Override
    public List<Evaluation> getAllEvaluations() {
        return null;
    }
}

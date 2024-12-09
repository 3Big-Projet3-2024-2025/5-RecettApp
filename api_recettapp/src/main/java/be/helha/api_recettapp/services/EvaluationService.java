package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

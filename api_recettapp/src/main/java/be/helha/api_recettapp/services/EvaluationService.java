package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }

    @Override
    public Evaluation updateEvaluation(Long id, Evaluation newEvaluation, boolean isAdmin) {
        return null;
    }
}

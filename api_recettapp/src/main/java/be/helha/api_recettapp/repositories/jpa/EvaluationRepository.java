package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}

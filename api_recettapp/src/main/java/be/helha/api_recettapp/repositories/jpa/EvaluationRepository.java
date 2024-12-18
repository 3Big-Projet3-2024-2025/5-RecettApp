package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByEntryId(Long entryId);

    List<Evaluation> findByRecipeId(Long recipeId);
}

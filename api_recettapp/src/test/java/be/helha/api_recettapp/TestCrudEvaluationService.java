package be.helha.api_recettapp;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import be.helha.api_recettapp.services.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestCrudEvaluationService {
    @Mock
    private EvaluationRepository evaluationRepository;

    @InjectMocks
    private EvaluationService evaluationService;

    private Evaluation evaluation1;
    private Evaluation evaluation2;

    private Entry entry;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création des objets Entry et Recipe
        entry = new Entry();
        entry.setId(1);

        recipe = new Recipe();
        recipe.setId(10);

        // Création des évaluations
        evaluation1 = new Evaluation();
        evaluation1.setId(1L);
        evaluation1.setRate(5);
        evaluation1.setEntry(entry);
        evaluation1.setRecipe(recipe);

        evaluation2 = new Evaluation();
        evaluation2.setId(2L);
        evaluation2.setRate(3);
        evaluation2.setEntry(entry);
        evaluation2.setRecipe(recipe);
    }

    @Test
    void testAddEvaluation() {
        when(evaluationRepository.save(evaluation1)).thenReturn(evaluation1);

        Evaluation savedEvaluation = evaluationService.addEvaluation(evaluation1);

        assertNotNull(savedEvaluation);
        assertEquals(evaluation1, savedEvaluation);
        verify(evaluationRepository, times(1)).save(evaluation1);
    }
}

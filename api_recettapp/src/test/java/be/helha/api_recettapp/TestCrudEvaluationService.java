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


import java.util.Arrays;
import java.util.List;

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

        entry = new Entry();
        entry.setId(1);

        recipe = new Recipe();
        recipe.setId(10);

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

    @Test
    void testDeleteEvaluationAsAdmin() {
        doNothing().when(evaluationRepository).deleteById(1L);

        assertDoesNotThrow(() -> evaluationService.deleteEvaluation(1L, true));
        verify(evaluationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEvaluationAsNonAdmin() {
        Exception exception = assertThrows(SecurityException.class, () -> {
            evaluationService.deleteEvaluation(1L, false);
        });

        assertEquals("Only an administrator can delete a review.", exception.getMessage());
        verify(evaluationRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetAllEvaluations() {
        when(evaluationRepository.findAll()).thenReturn(Arrays.asList(evaluation1, evaluation2));

        List<Evaluation> evaluations = evaluationService.getAllEvaluations();

        assertNotNull(evaluations);
        assertEquals(2, evaluations.size());
        verify(evaluationRepository, times(1)).findAll();
    }

    @Test
    void testGetEvaluationsByEntry() {
        when(evaluationRepository.findByEntryId(1L)).thenReturn(Arrays.asList(evaluation1, evaluation2));

        List<Evaluation> evaluations = evaluationService.getEvaluationsByEntry(1L);

        assertNotNull(evaluations);
        assertEquals(2, evaluations.size());
        verify(evaluationRepository, times(1)).findByEntryId(1L);
    }
}

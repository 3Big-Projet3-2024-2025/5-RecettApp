package be.helha.api_recettapp.services;

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


/**
 * Unit tests for the EvaluationService class.
 * This class tests the service layer methods for managing evaluations,
 * ensuring correct logic and interaction with the repository.
 */
public class TestCrudEvaluationService {
    @Mock
    private EvaluationRepository evaluationRepository;

    @InjectMocks
    private EvaluationService evaluationService;

    private Evaluation evaluation1;
    private Evaluation evaluation2;

    private Entry entry;
    private Recipe recipe;

    /**
     * Sets up the test environment before each test.
     * Initializes the mock repository, service, and test data.
     */
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

    /**
     * Tests the addEvaluation method.
     * Verifies that a new evaluation is saved successfully to the repository.
     */
    @Test
    void testAddEvaluation() {
        when(evaluationRepository.save(evaluation1)).thenReturn(evaluation1);

        Evaluation savedEvaluation = evaluationService.addEvaluation(evaluation1);

        assertNotNull(savedEvaluation);
        assertEquals(evaluation1, savedEvaluation);
        verify(evaluationRepository, times(1)).save(evaluation1);
    }

    /**
     * Tests the deleteEvaluation method for an administrator.
     * Ensures that the evaluation is deleted without any exceptions when the user is an admin.
     */
    @Test
    void testDeleteEvaluationAsAdmin() {
        doNothing().when(evaluationRepository).deleteById(1L);

        assertDoesNotThrow(() -> evaluationService.deleteEvaluation(1L, true));
        verify(evaluationRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests the deleteEvaluation method for a non-administrator.
     * Verifies that a SecurityException is thrown when a non-admin tries to delete an evaluation.
     */
    @Test
    void testDeleteEvaluationAsNonAdmin() {
        Exception exception = assertThrows(SecurityException.class, () -> {
            evaluationService.deleteEvaluation(1L, false);
        });

        assertEquals("Only an administrator can delete a review.", exception.getMessage());
        verify(evaluationRepository, never()).deleteById(anyLong());
    }

    /**
     * Tests the getAllEvaluations method.
     * Verifies that all evaluations are retrieved successfully from the repository.
     */
    @Test
    void testGetAllEvaluations() {
        when(evaluationRepository.findAll()).thenReturn(Arrays.asList(evaluation1, evaluation2));

        List<Evaluation> evaluations = evaluationService.getAllEvaluations();

        assertNotNull(evaluations);
        assertEquals(2, evaluations.size());
        verify(evaluationRepository, times(1)).findAll();
    }

    /**
     * Tests the getEvaluationsByEntry method.
     * Verifies that evaluations related to a specific entry ID are retrieved correctly.
     */
    @Test
    void testGetEvaluationsByEntry() {
        when(evaluationRepository.findByEntryId(1L)).thenReturn(Arrays.asList(evaluation1, evaluation2));

        List<Evaluation> evaluations = evaluationService.getEvaluationsByEntry(1L);

        assertNotNull(evaluations);
        assertEquals(2, evaluations.size());
        verify(evaluationRepository, times(1)).findByEntryId(1L);
    }

    /**
     * Tests the getEvaluationsByRecipe method.
     * Verifies that evaluations related to a specific recipe ID are retrieved correctly.
     */
    @Test
    void testGetEvaluationsByRecipe() {
        when(evaluationRepository.findByRecipeId(10L)).thenReturn(Arrays.asList(evaluation1));

        List<Evaluation> evaluations = evaluationService.getEvaluationsByRecipe(10L);

        assertNotNull(evaluations);
        assertEquals(1, evaluations.size());
        assertEquals(10L, evaluations.get(0).getRecipe().getId());
        verify(evaluationRepository, times(1)).findByRecipeId(10L);
    }
}

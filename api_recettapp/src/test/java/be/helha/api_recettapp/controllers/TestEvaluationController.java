package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.services.IEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the EvaluationController class.
 * This class tests the controller methods for managing evaluations (adding, deleting, and retrieving evaluations).
 */
public class TestEvaluationController {

    @Mock
    private IEvaluationService evaluationService;

    @InjectMocks
    private EvaluationController evaluationController;

    private Evaluation evaluation1;
    private Evaluation evaluation2;
    private Entry entry;
    private Recipe recipe;

    /**
     * Set up the test data before each test.
     * Initializes mock objects and test instances of Evaluation, Entry, and Recipe.
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
     * Test the addEvaluation method in the EvaluationController.
     * Verifies that a new evaluation is added successfully.
     */
    @Test
    void testAddEvaluation() {
        when(evaluationService.addEvaluation(evaluation1)).thenReturn(evaluation1);

        ResponseEntity<Evaluation> response = (ResponseEntity<Evaluation>) evaluationController.addEvaluation(evaluation1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(evaluation1, response.getBody());
        verify(evaluationService, times(1)).addEvaluation(evaluation1);
    }


    /**
     * Test the deleteEvaluation method when the user is an admin.
     * Verifies that the evaluation is deleted successfully for an admin.
     */
    @Test
    void testDeleteEvaluationAsAdmin() {
        doNothing().when(evaluationService).deleteEvaluation(1L, true);

        ResponseEntity<Void> response = evaluationController.deleteEvaluation(1L, true);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(evaluationService, times(1)).deleteEvaluation(1L, true);
    }

    /**
     * Test the deleteEvaluation method when the user is not an admin.
     * Verifies that a SecurityException is thrown when a non-admin tries to delete a review.
     */
    @Test
    void testDeleteEvaluationAsNonAdmin() {
        doThrow(new SecurityException("Only an administrator can delete a review."))
                .when(evaluationService).deleteEvaluation(1L, false);

        Exception exception = assertThrows(SecurityException.class, () -> {
            evaluationController.deleteEvaluation(1L, false);
        });

        assertEquals("Only an administrator can delete a review.", exception.getMessage());
        verify(evaluationService, times(1)).deleteEvaluation(1L, false);
    }




    /**
     * Test the getAllEvaluations method in the EvaluationController.
     * Verifies that all evaluations are returned successfully.
     */
    @Test
    void testGetAllEvaluations() {
        List<Evaluation> evaluations = Arrays.asList(evaluation1, evaluation2);

        when(evaluationService.getAllEvaluations()).thenReturn(evaluations);

        ResponseEntity<List<Evaluation>> response = evaluationController.getAllEvaluations();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(evaluations, response.getBody());
        verify(evaluationService, times(1)).getAllEvaluations();
    }

    /**
     * Test the getEvaluationsByEntry method in the EvaluationController.
     * Verifies that evaluations by entry ID are returned successfully.
     */
    @Test
    void testGetEvaluationsByEntry() {
        List<Evaluation> evaluations = Arrays.asList(evaluation1, evaluation2);

        when(evaluationService.getEvaluationsByEntry(1L)).thenReturn(evaluations);

        ResponseEntity<List<Evaluation>> response = evaluationController.getEvaluationsByEntry(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(evaluations, response.getBody());
        verify(evaluationService, times(1)).getEvaluationsByEntry(1L);
    }



}

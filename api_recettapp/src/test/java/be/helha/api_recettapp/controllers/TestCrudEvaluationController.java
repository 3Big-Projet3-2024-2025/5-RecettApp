package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Evaluation;
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

public class TestCrudEvaluationController {
    @Mock
    private IEvaluationService evaluationService;

    @InjectMocks
    private EvaluationController evaluationController;

    private Evaluation evaluation1;
    private Evaluation evaluation2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        evaluation1 = new Evaluation();
        evaluation1.setId(1L);
        evaluation1.setRate(5);

        evaluation2 = new Evaluation();
        evaluation2.setId(2L);
        evaluation2.setRate(3);
    }

    @Test
    void testAddEvaluation() {
        when(evaluationService.addEvaluation(eq(evaluation1))).thenReturn(evaluation1);

        ResponseEntity<Evaluation> response = evaluationController.addEvaluation(evaluation1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(evaluation1, response.getBody());
        verify(evaluationService, times(1)).addEvaluation(eq(evaluation1));
    }

    @Test
    void testDeleteEvaluationAsAdmin() {
        doNothing().when(evaluationService).deleteEvaluation(1L, true);

        ResponseEntity<Void> response = evaluationController.deleteEvaluation(1L, true);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(evaluationService, times(1)).deleteEvaluation(1L, true);
    }

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

    @Test
    void testGetAllEvaluations() {
        when(evaluationService.getAllEvaluations()).thenReturn(Arrays.asList(evaluation1, evaluation2));

        ResponseEntity<List<Evaluation>> response = evaluationController.getAllEvaluations();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(5, response.getBody().get(0).getRate());
        verify(evaluationService, times(1)).getAllEvaluations();
    }
}

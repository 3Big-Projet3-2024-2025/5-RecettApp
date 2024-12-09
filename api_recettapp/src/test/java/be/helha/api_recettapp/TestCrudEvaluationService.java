package be.helha.api_recettapp;

import be.helha.api_recettapp.controllers.EvaluationController;
import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import be.helha.api_recettapp.services.EvaluationService;
import be.helha.api_recettapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import be.helha.api_recettapp.controllers.UsersController;

import java.time.LocalDate;
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
        when(evaluationRepository.save(eq(evaluation1))).thenReturn(evaluation1);

        Evaluation result = evaluationService.addEvaluation(evaluation1);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(evaluationRepository, times(1)).save(eq(evaluation1));
    }

    @Test
    void testDeleteEvaluationAsAdmin() {
        doNothing().when(evaluationRepository).deleteById(1L);

        evaluationService.deleteEvaluation(1L, true);

        verify(evaluationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEvaluationAsNonAdmin() {
        assertThrows(SecurityException.class, () -> evaluationService.deleteEvaluation(1L, false));
    }

    @Test
    void testGetAllEvaluations() {
        when(evaluationRepository.findAll()).thenReturn(Arrays.asList(evaluation1, evaluation2));

        List<Evaluation> evaluations = evaluationService.getAllEvaluations();

        assertEquals(2, evaluations.size());
        assertEquals(5, evaluations.get(0).getRate());
        verify(evaluationRepository, times(1)).findAll();
    }
}

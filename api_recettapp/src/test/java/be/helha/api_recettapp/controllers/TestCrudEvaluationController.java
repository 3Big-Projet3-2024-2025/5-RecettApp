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

public class TestCrudEvaluationController {
    @Mock
    private IEvaluationService evaluationService;

    @InjectMocks
    private EvaluationController evaluationController;

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


    
}

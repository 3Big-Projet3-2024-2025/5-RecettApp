package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.services.IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Evaluation> addEvaluation(@RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.addEvaluation(evaluation));
    }

}

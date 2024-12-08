package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.services.IEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Evaluation> addEvaluation(@RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.addEvaluation(evaluation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id, @RequestParam boolean isAdmin) {
        evaluationService.deleteEvaluation(id, isAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

}

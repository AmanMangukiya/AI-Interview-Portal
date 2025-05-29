package com.aiinterview.controller;

import com.aiinterview.model.AnswerEvaluationRequest;
import com.aiinterview.model.AnswerEvaluationResponse;
import com.aiinterview.service.GenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class AnswerEvaluationController {

    private final GenAIService genAIService;

    @PostMapping("/evaluate")
    public ResponseEntity<AnswerEvaluationResponse> evaluateAnswer(
            @RequestBody AnswerEvaluationRequest request) {

        AnswerEvaluationResponse response = genAIService.evaluateAnswer(
                request.getQuestion(), request.getUserAnswer());

        return ResponseEntity.ok(response);
    }
}

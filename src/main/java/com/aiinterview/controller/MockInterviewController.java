package com.aiinterview.controller;

import com.aiinterview.model.MockInterviewRequest;
import com.aiinterview.model.MockInterviewResponse;
import com.aiinterview.service.GenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockInterviewController {

    private final GenAIService genAIService;
    @CrossOrigin(origins = "http://localhost:5174")
    @PostMapping("/generate")
    public ResponseEntity<MockInterviewResponse> generateQuestions(
            @RequestBody MockInterviewRequest request) {

        var questions = genAIService.generateInterviewQuestions(
                request.getTopic(), request.getDifficulty());

        return ResponseEntity.ok(new MockInterviewResponse(questions));
    }
}

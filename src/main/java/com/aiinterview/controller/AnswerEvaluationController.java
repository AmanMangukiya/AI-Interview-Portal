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
    public ResponseEntity<AnswerEvaluationResponse> evaluateAnswers(
            @RequestBody AnswerEvaluationRequest request) {

        StringBuilder feedbackBuilder = new StringBuilder();

        for (int i = 0; i < request.getQuestions().size(); i++) {
            String question = request.getQuestions().get(i);
            String userAnswer = request.getAnswers().get(i);

            AnswerEvaluationResponse response = genAIService.evaluateAnswer(question, userAnswer);

            feedbackBuilder.append("Q").append(i + 1).append(": ").append(question).append("\n");
            feedbackBuilder.append("Feedback: ").append(response.getFeedback()).append("\n\n");
        }

        return ResponseEntity.ok(new AnswerEvaluationResponse(feedbackBuilder.toString(), 0));
    }
//    public ResponseEntity<AnswerEvaluationResponse> evaluateAnswer(
//            @RequestBody AnswerEvaluationRequest request) {
//
//        AnswerEvaluationResponse response = genAIService.evaluateAnswer(
//                request.getQuestion(), request.getUserAnswer());
//
//        return ResponseEntity.ok(response);
//    }
}

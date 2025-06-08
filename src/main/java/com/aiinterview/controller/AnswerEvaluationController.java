package com.aiinterview.controller;

import com.aiinterview.model.AnswerEvaluationRequest;
import com.aiinterview.model.AnswerEvaluationResponse;
import com.aiinterview.model.QuizAttempt;
import com.aiinterview.repository.QuizAttemptRepository;
import com.aiinterview.repository.UserRepository;
import com.aiinterview.service.GenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class AnswerEvaluationController {

    private final GenAIService genAIService;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;

  //  @PostMapping("/evaluate")
  @PostMapping("/evaluate")

  public ResponseEntity<AnswerEvaluationResponse> evaluateAnswers(
          @RequestBody AnswerEvaluationRequest request, Principal principal) {

        StringBuilder feedbackBuilder = new StringBuilder();
        double totalScore = 0.0;

        String userEmail= principal.getName();


        for (int i = 0; i < request.getQuestions().size(); i++) {
            String question = request.getQuestions().get(i);
            String userAnswer = request.getAnswers().get(i);

            AnswerEvaluationResponse response = genAIService.evaluateAnswer(question, userAnswer);

            totalScore+=response.getScore();

            feedbackBuilder.append("Q").append(i + 1).append(": ").append(question).append("\n");
            feedbackBuilder.append("Feedback: ").append(response.getFeedback()).append("\n\n");
        }

        double avgScore = request.getQuestions().isEmpty() ? 0.0 : totalScore/request.getQuestions().size();

      QuizAttempt attempt= quizAttemptRepository.save(QuizAttempt.builder()
              .userId(userEmail)
              .topic(request.getTopic())
              .questions(request.getQuestions())
              .answers(request.getAnswers())
              .feedback(feedbackBuilder.toString())
              .averageScore(avgScore)
              .attemptedAt(LocalDateTime.now())
              .build());

      // Update User with new attempt ID
      userRepository.findByEmail(userEmail).ifPresent(user -> {
          user.getQuizAttemptIds().add(attempt.getId());
          userRepository.save(user);
      });
        return ResponseEntity.ok(new AnswerEvaluationResponse(feedbackBuilder.toString(), avgScore));
    }

}

package com.aiinterview.controller;

import com.aiinterview.model.MockInterviewRequest;
import com.aiinterview.model.MockInterviewResponse;
import com.aiinterview.model.QuizAttempt;
import com.aiinterview.model.User;
import com.aiinterview.repository.QuizAttemptRepository;
import com.aiinterview.service.GenAIService;
import com.aiinterview.service.QuizAttemptService;
import com.aiinterview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockInterviewController {

    private final GenAIService genAIService;
    private final UserService userService;
    private  final QuizAttemptService quizAttemptService;

    @PostMapping("/generate")
    public ResponseEntity<MockInterviewResponse> generateQuestions(
            @RequestBody MockInterviewRequest request) {

        var questions = genAIService.generateInterviewQuestions(
                request.getTopic(), request.getDifficulty());

        return ResponseEntity.ok(new MockInterviewResponse(questions));
    }
    @GetMapping("/quizes")
     public  ResponseEntity<List<QuizAttempt>> getAllQuize(Principal principal){

        String email= principal.getName();


         User user=userService.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));

         List<QuizAttempt> quizes=new ArrayList<>();

         List<String>quizId=user.getQuizAttemptIds();

            for(String id : quizId){

                QuizAttempt quizAttempt= quizAttemptService.findById(id).orElse(null);

                if(quizAttempt!=null) quizes.add(quizAttempt);
            }
            return ResponseEntity.ok(new ArrayList<>(quizes));

    }
}

package com.aiinterview.controller;

import com.aiinterview.model.User;
import com.aiinterview.repository.QuizAttemptRepository;
import com.aiinterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserRepository userRepository;
    private  final QuizAttemptRepository quizAttemptRepository;
    

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping("/remove")

    public ResponseEntity<?> deletUser(@RequestParam String email){

        User user= userRepository.findByEmail(email).orElse(null);

        if(user==null){
            return ResponseEntity.notFound().build();
        }

        List<String> quiz_ids=user.getQuizAttemptIds();

        if(quiz_ids!=null){

               for(String id:quiz_ids){
                   quizAttemptRepository.deleteById(id);
               }
        }

        userRepository.delete(user);
        return ResponseEntity.ok("User with email " + email + " deleted successfully.");

    }


}

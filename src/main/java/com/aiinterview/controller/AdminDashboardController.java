package com.aiinterview.controller;

import com.aiinterview.model.User;
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
    

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("totalUsers", userRepository.count());
//        stats.put("totalQuestions", questionRepository.count());
//        return ResponseEntity.ok(stats);
//    }
}

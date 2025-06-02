package com.aiinterview.controller;


import com.aiinterview.service.GenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class FaqController {

     private final GenAIService genAIService;
     @GetMapping("/faq")
    public ResponseEntity<List<String>> getFAQsWithAnswers(@RequestParam String topic) {
        List<String> faqs = genAIService.getFrequentlyAskedQuestionsWithAnswers(topic);
        return ResponseEntity.ok(faqs);
    }

}

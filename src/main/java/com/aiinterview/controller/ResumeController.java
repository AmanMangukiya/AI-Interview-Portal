package com.aiinterview.controller;

import com.aiinterview.model.ResumeFeedbackResponse;
import com.aiinterview.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/analyze")
    public ResponseEntity<ResumeFeedbackResponse> analyzeResume(
            @RequestParam("file") MultipartFile file) {

        ResumeFeedbackResponse feedback = resumeService.analyzeResume(file);
        return ResponseEntity.ok(feedback);
    }
}

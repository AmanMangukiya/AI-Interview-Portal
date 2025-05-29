package com.aiinterview.service;

import com.aiinterview.model.ResumeFeedbackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final GenAIService genAIService;

    public ResumeFeedbackResponse analyzeResume(MultipartFile file) {
        try {
            // Extract text using Apache Tika
            Tika tika = new Tika();
            String resumeText = tika.parseToString(file.getInputStream());

            // Build prompt
            String prompt = String.format(
                    "Analyze the following resume text and give improvement suggestions " +
                            "for a Java Developer role:\n\n%s", resumeText
            );

            // Send to GenAI
            String feedback = genAIService.sendPrompt(prompt, 300, 0.7);
            return new ResumeFeedbackResponse(feedback);

        } catch (Exception e) {
            log.error("Error processing resume", e);
            return new ResumeFeedbackResponse("Could not analyze resume.");
        }
    }
}

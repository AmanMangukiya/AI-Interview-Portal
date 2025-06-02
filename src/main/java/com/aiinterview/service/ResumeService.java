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

    public ResumeFeedbackResponse analyzeResume(MultipartFile file,String jobDescription) {
        try {
            // Extract text using Apache Tika
            Tika tika = new Tika();
            String resumeText = tika.parseToString(file.getInputStream());

            // Build prompt
//            String prompt = String.format(
//                    "Analyze the following resume text and give improvement suggestions " +
//                            "for a Java Developer role:\n\n%s", resumeText
//            );

            String prompt = String.format(
                    "You are an expert career coach and resume evaluator.\n\n" +
                            "Given the following resume content:\n\n%s\n\n" +
                            "And the following job description:\n\n%s\n\n" +
                            "Please analyze how well the resume aligns with the job description. Provide detailed and constructive suggestions to improve the resume, such as:\n" +
                            "- Skills or keywords that are missing or need more emphasis\n" +
                            "- Experience gaps or areas that need elaboration\n" +
                            "- Formatting, structure, or clarity improvements\n" +
                            "- Suggestions for tailoring the resume specifically to this job\n\n" +
                            "Respond in a professional tone and format your feedback in bullet points or sections.",
                    resumeText,
                    jobDescription
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

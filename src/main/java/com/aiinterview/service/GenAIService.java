package com.aiinterview.service;

import com.aiinterview.model.AnswerEvaluationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenAIService {

    @Value("${genai.api.url}")
    private String genaiApiUrl;

    @Value("${genai.api.key}")
    private String genaiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. Generate interview questions
    public List<String> generateInterviewQuestions(String topic, String difficulty) {


        String prompt = String.format(
                "Generate  %s level interview questions on this %s. Only list of 2 questions,no extra line of text , no answers.",
                difficulty, topic
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mistral-7b-instruct"); // ✅ valid model ID
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(genaiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    genaiApiUrl, HttpMethod.POST, request, Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String content = extractText(response.getBody());
                return Arrays.stream(content.split("\n"))
                        .map(String::trim)
                        .filter(q -> !q.isEmpty() && q.matches(".*[a-zA-Z].*")) // filters out empty, numeric-only, or whitespace lines
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error calling GenAI API", e);
        }

        return List.of("Could not fetch questions from AI.");
    }

    // 2. Evaluate a user's answer
    public AnswerEvaluationResponse evaluateAnswer(String question, String userAnswer) {
        String prompt = String.format(
                "Evaluate the following answer to the interview question.\n" +
                        "Question: %s\n" +
                        "User's Answer: %s\n" +
                        "Give a score out of 10 and a short improvement suggestion.",
                question, userAnswer
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mistral-7b-instruct");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 200);
        requestBody.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(genaiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    genaiApiUrl, HttpMethod.POST, request, Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String content = extractText(response.getBody());
                int score = extractScore(content);
                return new AnswerEvaluationResponse(content, score);
            }
        } catch (Exception e) {
            log.error("AI evaluation failed", e);
        }

        return new AnswerEvaluationResponse("Evaluation failed. Try again later.", 0);
    }

    // Helper: Extracts content from GenAI API response
    private String extractText(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            log.error("Failed to extract text from GenAI response", e);
            return "Invalid response from AI.";
        }
    }

    // Helper: Tries to extract a numeric score from response text
    private int extractScore(String response) {
        try {
            return Integer.parseInt(response.replaceAll("[^0-9]", "").substring(0, 2));
        } catch (Exception e) {
            return 0;
        }
    }

    // Optional generic prompt sender
    public String sendPrompt(String prompt, int maxTokens, double temperature) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mistral-7b-instruct");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", temperature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(genaiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    genaiApiUrl, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return extractText(response.getBody());
            }
        } catch (Exception e) {
            log.error("GenAI prompt error", e);
        }
        return "Failed to get feedback from AI.";
    }

    // 3. Generate most frequently asked questions with answers
    public List<String> getFrequentlyAskedQuestionsWithAnswers(String topic) {
//        String prompt = String.format(
//                "Give 3 of the most frequently asked interview questions on the topic '%s' along with their model answers. " +
//                        "Format each question with its answer. Avoid extra intro or summary text.",
//                topic
//        );

        String prompt = String.format(
                "Act as an experienced technical interviewer. Generate 5 of the most commonly asked interview questions on the topic '%s'. " +
                        "For each question, provide a detailed, accurate, and concise model answer suitable for a job interview. " +
                        "Format the response clearly as:\n\n" +
                        "Q1: <question>\nA1: <answer>\n\nQ2: <question>\nA2: <answer>\n\nQ3: <question>\nA3: <answer>\n\n" +
                        "Avoid any introductions or conclusions. Focus only on questions and answers.",
                topic
        );


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mistral-7b-instruct");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 1000);  // Larger because answers are included
        requestBody.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(genaiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    genaiApiUrl, HttpMethod.POST, request, Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String content = extractText(response.getBody());
                return Arrays.stream(content.split("\n\n")) // Assuming AI separates Q&As with double newlines
                        .map(String::trim)
                        .filter(qa -> !qa.isEmpty())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error fetching FAQ with answers from AI", e);
        }

        return List.of("Unable to fetch FAQs with answers from AI.");
    }

}

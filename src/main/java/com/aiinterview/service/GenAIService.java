package com.aiinterview.service;

import com.aiinterview.model.AnswerEvaluationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiChatOptions;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenAIService {

    @Autowired
    private ChatClient chatClient;

    public List<String> generateInterviewQuestions(String topic, String difficulty) {
        String s = String.format(
                "Generate %s level interview questions on this %s. Only list of 2 questions, no extra line of text, no answers.",
                difficulty, topic
        );

        Prompt prompt = new Prompt(
                s,
                OpenAiChatOptions.builder()
                        .withModel("deepseek/deepseek-r1-distill-llama-70b")
                        .withTemperature(0.7f)
                        .build()
        );

        try {
            String content = chatClient.call(prompt).getResult().getOutput().getContent();
            return List.of(content.split("\n")).stream()
                    .map(String::trim)
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("AI Error: Unable to generate questions. " + e.getMessage());
        }
    }
    public AnswerEvaluationResponse evaluateAnswer(String question, String userAnswer) {
        String s = String.format(
                "Evaluate the following answer to the interview question.\n" +
                        "Question: %s\nUser's Answer: %s\n" +
                        "Give a score out of 10 and a short improvement suggestion and right answer.",
                question, userAnswer
        );

        Prompt prompt = new Prompt(
                s,
                OpenAiChatOptions.builder()
                        .withModel("deepseek/deepseek-r1-distill-llama-70b")
                        .withTemperature(0.7f)
                        .build()
        );

        try {
            String content = chatClient.call(prompt).getResult().getOutput().getContent();
            int score = extractScore(content);
            return new AnswerEvaluationResponse(content, score);
        } catch (Exception e) {
            e.printStackTrace();
            return new AnswerEvaluationResponse("AI Error: Unable to evaluate answer. " + e.getMessage(), 0);
        }
    }

    public List<String> getFrequentlyAskedQuestionsWithAnswers(String topic) {
        String s = String.format(
                "Act as an experienced technical interviewer. Generate 5 of the most commonly asked interview questions on the topic '%s'. " +
                        "For each question, provide a detailed, accurate, and concise model answer suitable for a job interview. " +
                        "Format the response clearly as:\n\n" +
                        "Q1: <question>\nA1: <answer>\n\nQ2: <question>\nA2: <answer>\n\nQ3: <question>\nA3: <answer>\n\n" +
                        "Avoid any introductions or conclusions. Focus only on questions and answers.",
                topic
        );

        Prompt prompt = new Prompt(
                s,
                OpenAiChatOptions.builder()
                        .withModel("deepseek/deepseek-r1-distill-llama-70b")
                        .withTemperature(0.7f)
                        .build()
        );
        try {
            String response = chatClient.call(prompt).getResult().getOutput().getContent();
            return List.of(response.split("\n\n")).stream()
                    .map(String::trim)
                    .filter(qa -> !qa.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("AI Error: Failed to fetch questions. " + e.getMessage());
        }
    }

    public String sendPrompt(String s, int maxTokens, double temperature) {

        Prompt prompt = new Prompt(
                s,
                OpenAiChatOptions.builder()
                        .withModel("deepseek/deepseek-r1-distill-llama-70b")
                        .withTemperature(0.7f)
                        .build()
        );

        try {
            return chatClient.call(prompt).getResult().getOutput().getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return "AI Error: " + e.getMessage();
        }
    }

    public int extractScore(String response) {
        try {
            // Look for a number followed by '/10'
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)/10").matcher(response);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        } catch (Exception ignored) {
        }
        return 0; // fallback
    }

}


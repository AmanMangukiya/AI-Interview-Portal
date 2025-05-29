package com.aiinterview.model;

import lombok.Data;

@Data
public class MockInterviewRequest {
    private String topic;       // e.g., "Spring Boot"
    private String difficulty;  // e.g., "easy", "medium", "hard"
}

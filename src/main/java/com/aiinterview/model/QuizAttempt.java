package com.aiinterview.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "quiz_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {
    @Id
    private String id;
    private String topic;
    private String userId;
    private List<String> questions;
    private List<String> answers;
    private String feedback;
    private double averageScore;
    private LocalDateTime attemptedAt;
}

package com.aiinterview.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerEvaluationResponse {
    private String feedback;
    private int score; // Score out of 10
}

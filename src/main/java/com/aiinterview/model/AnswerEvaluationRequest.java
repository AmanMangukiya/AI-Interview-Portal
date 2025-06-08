package com.aiinterview.model;

import lombok.Data;

import java.util.List;

@Data
public class AnswerEvaluationRequest {
//    private String question;
//    private String userAnswer;
    private String topic;
    private List<String> questions;
    private List<String> answers;
}

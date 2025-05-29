package com.aiinterview.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MockInterviewResponse {
    private List<String> questions;
}

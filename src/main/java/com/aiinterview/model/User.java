package com.aiinterview.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    private String name;
    @Indexed(unique = true)  // Unique index
    private String email;

    private String password;

    private Set<String> roles; // e.g., USER, ADMIN
    private List<String> quizAttemptIds= new ArrayList<>();; // NEW FIELD
    private String bio;
    private String profileImageUrl;
}

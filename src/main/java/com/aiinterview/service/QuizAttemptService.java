package com.aiinterview.service;
import com.aiinterview.model.QuizAttempt;
import com.aiinterview.repository.QuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;

    public Optional<QuizAttempt> findById(String Id){
         return quizAttemptRepository.findById(Id);
    }


    public void deleteById (String Id){
        quizAttemptRepository.deleteById(Id);
    }
}

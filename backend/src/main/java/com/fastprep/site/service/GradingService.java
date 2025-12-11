package com.fastprep.site.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GradingService {

    public int gradeExam(Map<Integer, List<String>> userAnswers, List<Map<String, Object>> questions) {
        int score = 0;
        int questionIndex = 0;
        
        for (int qNum : userAnswers.keySet()) {
            if (isCorrect(questionIndex, userAnswers.get(qNum), questions)) {
                score++;
            }
            questionIndex++;
        }
        return score;
    }

    private boolean isCorrect(int questionIndex, List<String> userChoice, List<Map<String, Object>> questions) {
        if (questionIndex >= questions.size()) {
            return false;
        }
        
        Map<String, Object> question = questions.get(questionIndex);
        String correctAnswer = (String) question.get("correct_answer_keys");
        
        if (correctAnswer.length() == 1) {
            return gradeSingleChoice(correctAnswer, userChoice);
        } else {
            return gradeMultipleChoice(correctAnswer, userChoice);
        }
    }

    private boolean gradeSingleChoice(String correctAnswer, List<String> userChoice) {
        return !userChoice.isEmpty() && correctAnswer.equals(userChoice.get(0));
    }

    private boolean gradeMultipleChoice(String correctAnswer, List<String> userChoice) {
        List<String> correctAnswers = Arrays.asList(correctAnswer.split(","));
        correctAnswers = correctAnswers.stream().map(String::trim).toList();
        return correctAnswers.equals(userChoice);
    }
}
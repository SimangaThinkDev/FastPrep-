package com.fastprep.site.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service responsible for grading AWS certification practice exams.
 * Handles both single-choice and multiple-choice question types with appropriate scoring logic.
 * Extracted from static utility class for better testability and dependency injection.
 */
@Service
public class GradingService {

    /**
     * Grades a complete exam by comparing user answers against correct answers.
     * Supports both single-choice (radio) and multiple-choice (checkbox) questions.
     * 
     * @param userAnswers map of question numbers to selected answer choices
     * @param questions list of question objects containing correct answers and metadata
     * @return total score as number of correctly answered questions
     */
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

    /**
     * Determines if a user's answer to a specific question is correct.
     * Automatically detects question type based on correct answer format and delegates to appropriate grading method.
     * 
     * @param questionIndex zero-based index of the question in the questions list
     * @param userChoice list of user-selected answer keys
     * @param questions complete list of question objects
     * @return true if the user's answer matches the correct answer(s)
     */
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

    /**
     * Grades single-choice questions (radio button type).
     * User must select exactly one option that matches the correct answer.
     * 
     * @param correctAnswer single character representing the correct option (e.g., "A")
     * @param userChoice list containing user's selected option
     * @return true if user selected the correct single option
     */
    private boolean gradeSingleChoice(String correctAnswer, List<String> userChoice) {
        return !userChoice.isEmpty() && correctAnswer.equals(userChoice.get(0));
    }

    /**
     * Grades multiple-choice questions (checkbox type).
     * User must select all correct options and no incorrect options for full credit.
     * 
     * @param correctAnswer comma-separated string of correct options (e.g., "A,C,D")
     * @param userChoice list of user-selected options
     * @return true if user's selections exactly match all correct answers
     */
    private boolean gradeMultipleChoice(String correctAnswer, List<String> userChoice) {
        List<String> correctAnswers = Arrays.asList(correctAnswer.split(","));
        correctAnswers = correctAnswers.stream().map(String::trim).toList();
        return correctAnswers.equals(userChoice);
    }
}
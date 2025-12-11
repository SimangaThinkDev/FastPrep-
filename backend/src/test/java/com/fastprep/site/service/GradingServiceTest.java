package com.fastprep.site.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GradingServiceTest {

    private final GradingService gradingService = new GradingService();

    @Test
    void testGradeSingleChoiceCorrect() {
        Map<Integer, List<String>> userAnswers = Map.of(1, List.of("A"));
        List<Map<String, Object>> questions = List.of(
            Map.of("correct_answer_keys", "A", "correct_answer_text", "Correct answer")
        );

        int score = gradingService.gradeExam(userAnswers, questions);

        assertEquals(1, score);
    }

    @Test
    void testGradeSingleChoiceIncorrect() {
        Map<Integer, List<String>> userAnswers = Map.of(1, List.of("B"));
        List<Map<String, Object>> questions = List.of(
            Map.of("correct_answer_keys", "A", "correct_answer_text", "Correct answer")
        );

        int score = gradingService.gradeExam(userAnswers, questions);

        assertEquals(0, score);
    }

    @Test
    void testGradeMultipleChoiceCorrect() {
        Map<Integer, List<String>> userAnswers = Map.of(1, List.of("A", "C"));
        List<Map<String, Object>> questions = List.of(
            Map.of("correct_answer_keys", "A, C", "correct_answer_text", "Correct answers")
        );

        int score = gradingService.gradeExam(userAnswers, questions);

        assertEquals(1, score);
    }

    @Test
    void testGradeMultipleChoiceIncorrect() {
        Map<Integer, List<String>> userAnswers = Map.of(1, List.of("A", "B"));
        List<Map<String, Object>> questions = List.of(
            Map.of("correct_answer_keys", "A, C", "correct_answer_text", "Correct answers")
        );

        int score = gradingService.gradeExam(userAnswers, questions);

        assertEquals(0, score);
    }

    @Test
    void testGradeMultipleQuestions() {
        Map<Integer, List<String>> userAnswers = Map.of(
            1, List.of("A"),
            2, List.of("C"),
            3, List.of("A", "C")
        );
        List<Map<String, Object>> questions = List.of(
            Map.of("correct_answer_keys", "A"),
            Map.of("correct_answer_keys", "C"),
            Map.of("correct_answer_keys", "A, C")
        );

        int score = gradingService.gradeExam(userAnswers, questions);

        assertEquals(3, score); // All questions correct
    }
}
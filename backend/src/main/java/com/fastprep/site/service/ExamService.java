package com.fastprep.site.service;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamLevel;
import com.fastprep.site.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // Get a single exam by its numeric examId
    public Exam getExam(int examId) {
        return examRepository.findByExamId(examId);
    }

    // Save or update an exam
    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    // Get all exams
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // Get exams by courseName (formerly "level string")
    public List<Exam> getExamsByCourseName(String courseName) {
        return examRepository.findByMetadataCourseName(courseName);
    }

    // Get exams by level
    public List<Exam> getExamsByLevel(ExamLevel level) {
        return examRepository.findByMetadataLevel(level);
    }

    // Get exams by numeric difficulty (for backward compatibility)
    public List<Exam> getExamsByDifficulty(int difficulty) {
        return examRepository.findByMetadataDifficulty(difficulty);
    }

    // Get all exams ordered by level and difficulty
    public List<Exam> getAllExamsOrdered() {
        return examRepository.findAllByOrderByMetadataLevelAscMetadataDifficultyAsc();
    }
}

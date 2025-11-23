package com.fastprep.site.service;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamRepository;
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

    // Get exams by numeric level (for ordering / pagination)
    public List<Exam> getExamsByLevel(int level) {
        return examRepository.findByMetadataLevel(level);
    }

    // Get all exams ordered by level and difficulty
    public List<Exam> getAllExamsOrdered() {
        return examRepository.findAllByOrderByMetadataLevelAscMetadataDifficultyAsc();
    }
}

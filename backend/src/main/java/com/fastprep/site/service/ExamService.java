package com.fastprep.site.service;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamLevel;
import com.fastprep.site.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing AWS certification practice exams.
 * Provides business logic for exam retrieval, filtering, and persistence operations.
 * Acts as an intermediary between the controller layer and repository layer.
 */
@Service
public class ExamService {

    private final ExamRepository examRepository;

    /**
     * Constructor for dependency injection of ExamRepository.
     * 
     * @param examRepository the repository for exam data access
     */
    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * Retrieves a single exam by its unique numeric identifier.
     * 
     * @param examId the unique numeric identifier of the exam
     * @return the exam with the specified ID, or null if not found
     */
    public Exam getExam(int examId) {
        return examRepository.findByExamId(examId);
    }

    /**
     * Persists an exam to the database (create or update).
     * 
     * @param exam the exam entity to save
     * @return the saved exam with any generated fields populated
     */
    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    /**
     * Retrieves all available exams from the database.
     * 
     * @return list of all exams, empty list if none exist
     */
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    /**
     * Retrieves exams filtered by course name (e.g., "AWS Certified Cloud Practitioner").
     * 
     * @param courseName the name of the certification course
     * @return list of exams matching the course name
     */
    public List<Exam> getExamsByCourseName(String courseName) {
        return examRepository.findByMetadataCourseName(courseName);
    }

    /**
     * Retrieves exams filtered by certification level enum.
     * 
     * @param level the ExamLevel enum (FOUNDATIONAL, ASSOCIATE, PROFESSIONAL, SPECIALTY)
     * @return list of exams matching the specified level
     */
    public List<Exam> getExamsByLevel(ExamLevel level) {
        return examRepository.findByMetadataLevel(level);
    }

    /**
     * Retrieves exams by numeric difficulty for backward compatibility.
     * Legacy method supporting old numeric difficulty system.
     * 
     * @param difficulty numeric difficulty level (1-4)
     * @return list of exams matching the numeric difficulty
     */
    public List<Exam> getExamsByDifficulty(int difficulty) {
        return examRepository.findByMetadataDifficulty(difficulty);
    }

    /**
     * Retrieves all exams ordered by level and difficulty for consistent presentation.
     * 
     * @return list of all exams sorted by level (ascending) then difficulty (ascending)
     */
    public List<Exam> getAllExamsOrdered() {
        return examRepository.findAllByOrderByMetadataLevelAscMetadataDifficultyAsc();
    }
}

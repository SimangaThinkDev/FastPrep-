package com.fastprep.site.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ExamRepository extends MongoRepository<Exam, String> {

    @Query( "{ 'exam_metadata.exam_id': ?0 }" )
    Exam findByExamId(int examId);

    // Find by numeric level
    List<Exam> findByMetadataLevel(int level);

    // Find by courseName
    @Query("{ 'exam_metadata.courseName': ?0 }")
    List<Exam> findByMetadataCourseName(String courseName);

    // Optional: find all exams ordered by level and difficulty
    List<Exam> findAllByOrderByMetadataLevelAscMetadataDifficultyAsc();
}

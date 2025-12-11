package com.fastprep.site.repository;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ExamRepository extends MongoRepository<Exam, String> {

    @Query( "{ 'exam_metadata.exam_id': ?0 }" )
    Exam findByExamId(int examId);

    @Query("{ 'exam_metadata.courseName': ?0 }")
    List<Exam> findByMetadataCourseName(String courseName);

    @Query("{ 'exam_metadata.level': ?0 }")
    List<Exam> findByMetadataLevel(ExamLevel level);

    @Query("{ 'exam_metadata.difficulty': ?0 }")
    List<Exam> findByMetadataDifficulty(int difficulty);

    @Query(value = "{}", sort = "{ 'exam_metadata.difficulty': 1 }")
    List<Exam> findAllByOrderByMetadataLevelAscMetadataDifficultyAsc();
}
package com.fastprep.site.repository;

import com.fastprep.site.model.ExamReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamReviewRepository extends MongoRepository<ExamReview, String> {
    
    List<ExamReview> findByUserIdOrderByCreatedAtDesc(String userId);
    
    Optional<ExamReview> findFirstByUserIdOrderByCreatedAtDesc(String userId);
}
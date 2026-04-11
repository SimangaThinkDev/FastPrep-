package com.fastprep.site.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "exam_reviews")
public class ExamReview {
    
    @Id
    private String id;
    private String userId;
    private String examId;
    private int score;
    private String aiReview;
    private LocalDateTime createdAt;
    
    public ExamReview() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ExamReview(String userId, String examId, int score, String aiReview) {
        this.userId = userId;
        this.examId = examId;
        this.score = score;
        this.aiReview = aiReview;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public String getAiReview() { return aiReview; }
    public void setAiReview(String aiReview) { this.aiReview = aiReview; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
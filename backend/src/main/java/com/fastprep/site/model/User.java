package com.fastprep.site.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    private String email;
    private String fullName;
    private String password;
    private LocalDateTime createdAt;
    private List<ExamAttempt> examHistory;
    private UserStats stats;

    public User() {
        this.createdAt = LocalDateTime.now();
        this.examHistory = new ArrayList<>();
        this.stats = new UserStats();
    }

    public User(String email, String fullName, String password) {
        this();
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ExamAttempt> getExamHistory() { return examHistory; }
    public void setExamHistory(List<ExamAttempt> examHistory) { this.examHistory = examHistory; }

    public UserStats getStats() { return stats; }
    public void setStats(UserStats stats) { this.stats = stats; }

    // Nested classes for exam data
    public static class ExamAttempt {
        private int examId;
        private String examTitle;
        private int score;
        private int totalQuestions;
        private LocalDateTime completedAt;
        private long durationMinutes;

        public ExamAttempt() {}

        public ExamAttempt(int examId, String examTitle, int score, int totalQuestions, long durationMinutes) {
            this.examId = examId;
            this.examTitle = examTitle;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.durationMinutes = durationMinutes;
            this.completedAt = LocalDateTime.now();
        }

        // Getters and Setters
        public int getExamId() { return examId; }
        public void setExamId(int examId) { this.examId = examId; }

        public String getExamTitle() { return examTitle; }
        public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }

        public int getTotalQuestions() { return totalQuestions; }
        public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

        public LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

        public long getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(long durationMinutes) { this.durationMinutes = durationMinutes; }

        public int getPercentage() {
            return totalQuestions > 0 ? (score * 100) / totalQuestions : 0;
        }
    }

    public static class UserStats {
        private int totalExamsCompleted;
        private double averageScore;
        private int totalStudyHours;
        private int certificationsReady;

        public UserStats() {
            this.totalExamsCompleted = 0;
            this.averageScore = 0.0;
            this.totalStudyHours = 0;
            this.certificationsReady = 0;
        }

        // Getters and Setters
        public int getTotalExamsCompleted() { return totalExamsCompleted; }
        public void setTotalExamsCompleted(int totalExamsCompleted) { this.totalExamsCompleted = totalExamsCompleted; }

        public double getAverageScore() { return averageScore; }
        public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

        public int getTotalStudyHours() { return totalStudyHours; }
        public void setTotalStudyHours(int totalStudyHours) { this.totalStudyHours = totalStudyHours; }

        public int getCertificationsReady() { return certificationsReady; }
        public void setCertificationsReady(int certificationsReady) { this.certificationsReady = certificationsReady; }
    }
}
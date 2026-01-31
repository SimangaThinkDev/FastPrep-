package com.fastprep.site.service;

import com.fastprep.site.model.User;
import com.fastprep.site.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String email, String fullName, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }
        
        User user = new User(email, fullName, password);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User authenticateUser(String email, String password) {
        Optional<User> userOpt = findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        return null;
    }

    public User getOrCreateUser(String email, String fullName) {
        // Try to find existing user first
        Optional<User> existingUser = findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        // Create new user with empty data (no mock data)
        User user = new User(email, fullName, "password");
        return userRepository.save(user);
    }

    public void addExamAttempt(String userId, int examId, String examTitle, int score, int totalQuestions, long durationMinutes) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            User.ExamAttempt attempt = new User.ExamAttempt(examId, examTitle, score, totalQuestions, durationMinutes);
            user.getExamHistory().add(attempt);
            
            // Update stats
            updateUserStats(user);
            
            userRepository.save(user);
        }
    }

    private void updateUserStats(User user) {
        List<User.ExamAttempt> history = user.getExamHistory();
        if (history.isEmpty()) return;
        
        User.UserStats stats = user.getStats();
        stats.setTotalExamsCompleted(history.size());
        
        double avgScore = history.stream()
                .mapToInt(User.ExamAttempt::getPercentage)
                .average()
                .orElse(0.0);
        stats.setAverageScore(Math.round(avgScore * 100.0) / 100.0);
        
        int totalHours = (int) history.stream()
                .mapToLong(User.ExamAttempt::getDurationMinutes)
                .sum() / 60;
        stats.setTotalStudyHours(totalHours);
        
        // Mock certification readiness based on average score
        stats.setCertificationsReady(avgScore >= 70 ? 3 : avgScore >= 60 ? 2 : 1);
    }
}
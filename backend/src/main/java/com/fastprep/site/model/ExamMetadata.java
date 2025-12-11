package com.fastprep.site.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class ExamMetadata {

    @Field("exam_id")
    private int examId;

    private String title;

    private String courseName;

    private ExamLevel level;

    @Field("difficulty")
    private int difficulty;  // Keep for backward compatibility, will be converted to level

    private List<String> tags;

    private String description;

    // getters and setters
    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public ExamLevel getLevel() { 
        if (level == null && difficulty > 0) {
            try {
                level = ExamLevel.fromNumeric(difficulty);
            } catch (IllegalArgumentException e) {
                level = null;
            }
        }
        if (level == null && courseName != null) {
            level = ExamLevel.fromCourseName(courseName);
        }
        return level != null ? level : ExamLevel.FOUNDATIONAL; 
    }
    public void setLevel(ExamLevel level) { this.level = level; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { 
        this.difficulty = difficulty;
        if (difficulty > 0) {
            try {
                this.level = ExamLevel.fromNumeric(difficulty);
            } catch (IllegalArgumentException e) {
                this.level = ExamLevel.FOUNDATIONAL;
            }
        }
    }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

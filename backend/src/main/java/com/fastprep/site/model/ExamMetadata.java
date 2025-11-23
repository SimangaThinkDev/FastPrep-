package com.fastprep.site.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class ExamMetadata {

    @Field("exam_id")
    private int examId;

    private String title;

    private String courseName;  // previously was level string

    private int level;          // numeric level for ordering

    private int difficulty;     // numeric difficulty now

    private List<String> tags;

    private String description;

    // getters and setters
    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

package com.fastprep.site.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = "exams")
public class Exam {

    @Id
    private String id;

    @Field("exam_metadata")
    private ExamMetadata exam_metadata;

    private List<Map<String, Object>> questions;

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ExamMetadata getMetadata() { return exam_metadata; }
    public void setMetadata(ExamMetadata exam_metadata) { this.exam_metadata = exam_metadata; }

    public List<Map<String, Object>> getQuestions() { return questions; }
    public void setQuestions(List<Map<String, Object>> questions) { this.questions = questions; }
}

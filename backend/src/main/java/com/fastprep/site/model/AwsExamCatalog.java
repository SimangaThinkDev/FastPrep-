package com.fastprep.site.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AwsExamCatalog {
    
    public static class ExamInfo {
        private final String name;
        private final ExamLevel level;
        private final boolean available;
        
        public ExamInfo(String name, ExamLevel level, boolean available) {
            this.name = name;
            this.level = level;
            this.available = available;
        }
        
        public String getName() { return name; }
        public ExamLevel getLevel() { return level; }
        public boolean isAvailable() { return available; }
    }
    
    private static final List<ExamInfo> ALL_EXAMS = Arrays.asList(
        // Foundational
        new ExamInfo("AWS Cloud Practitioner", ExamLevel.FOUNDATIONAL, true),
        
        // Associate
        new ExamInfo("Solutions Architect - Associate", ExamLevel.ASSOCIATE, false),
        new ExamInfo("Developer - Associate", ExamLevel.ASSOCIATE, false),
        new ExamInfo("SysOps Administrator - Associate", ExamLevel.ASSOCIATE, false),
        new ExamInfo("Data Engineer - Associate", ExamLevel.ASSOCIATE, false),
        new ExamInfo("Machine Learning Engineer - Associate", ExamLevel.ASSOCIATE, false),
        new ExamInfo("AI Practitioner", ExamLevel.ASSOCIATE, false),
        
        // Professional
        new ExamInfo("Solutions Architect - Professional", ExamLevel.PROFESSIONAL, false),
        new ExamInfo("DevOps Engineer - Professional", ExamLevel.PROFESSIONAL, false),
        
        // Specialty
        new ExamInfo("Security - Specialty", ExamLevel.SPECIALTY, false),
        new ExamInfo("Database - Specialty", ExamLevel.SPECIALTY, false),
        new ExamInfo("Machine Learning - Specialty", ExamLevel.SPECIALTY, false),
        new ExamInfo("Advanced Networking - Specialty", ExamLevel.SPECIALTY, false),
        new ExamInfo("Data Analytics - Specialty", ExamLevel.SPECIALTY, false),
        new ExamInfo("SAP on AWS - Specialty", ExamLevel.SPECIALTY, false)
    );
    
    public static Map<ExamLevel, List<ExamInfo>> getExamsByLevel() {
        return ALL_EXAMS.stream()
            .collect(Collectors.groupingBy(ExamInfo::getLevel));
    }
    
    public static List<ExamInfo> getAllExams() {
        return ALL_EXAMS;
    }
}
package com.fastprep.site.model;

public enum ExamLevel {
    FOUNDATIONAL("Foundational", 1),
    ASSOCIATE("Associate", 2),
    PROFESSIONAL("Professional", 3),
    SPECIALTY("Specialty", 4);

    private final String displayName;
    private final int numericValue;

    ExamLevel(String displayName, int numericValue) {
        this.displayName = displayName;
        this.numericValue = numericValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public static ExamLevel fromNumeric(int value) {
        for (ExamLevel level : values()) {
            if (level.numericValue == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid exam level: " + value);
    }

    public static ExamLevel fromCourseName(String courseName) {
        if (courseName == null) {
            return FOUNDATIONAL;
        }
        String lowerName = courseName.toLowerCase();
        if (lowerName.contains("practitioner")) {
            return FOUNDATIONAL;
        } else if (lowerName.contains("associate")) {
            return ASSOCIATE;
        } else if (lowerName.contains("professional")) {
            return PROFESSIONAL;
        } else if (lowerName.contains("specialty")) {
            return SPECIALTY;
        }
        return FOUNDATIONAL; // default
    }
}
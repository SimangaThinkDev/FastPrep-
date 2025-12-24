package com.fastprep.site.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * Utility class for processing HTTP request parameters related to exam submissions.
 * Provides static methods for extracting and parsing user answer data from form submissions.
 */
public class RequestUtils {

    /**
     * Extracts user answers from HTTP request parameters submitted via exam form.
     * Parses both single-choice (radio) and multiple-choice (checkbox) question responses.
     * Handles parameter naming conventions: "question_N" for single choice, "question_N[]" for multiple choice.
     * 
     * @param request the HttpServletRequest containing form submission data
     * @return map where keys are question numbers and values are lists of selected answer options
     *         For single-choice questions, list contains one element
     *         For multiple-choice questions, list contains all selected options
     *         Empty list indicates no selection for that question
     */
    public static Map<Integer, List<String>> extractUserAnswers(HttpServletRequest request) {
        Map<Integer, List<String>> userAnswers = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (key.startsWith("question_")) {
                String qNumStr = key.replace("question_", "").replace("[]", "");
                int qNum = Integer.parseInt(qNumStr);

                List<String> selected = Arrays.asList(values);
                if (selected.size() == 1 && selected.get(0).isEmpty()) {
                    selected = Collections.emptyList();
                }
                userAnswers.put(qNum, selected);
            }
        });

        return userAnswers;
    }
}
package com.fastprep.site.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class RequestUtils {

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
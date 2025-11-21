package com.fastprep.site.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Tools {

    /**
     * Extracts user data from the form that is being sent
     * after each exam in frontend
     *
     * @param request the request object that is received by the
     *                controller from front-end
     * @return a map of the question numbers and the corresponding
     *         user answers
     */
    public static Map<Integer, List<String> > extractUserData(HttpServletRequest request ) {
        Map<Integer, List<String>> userAnswers = new HashMap<>();

        // Loop through all parameters
        request.getParameterMap().forEach((key, values) -> {
            if (key.startsWith("question_")) {
                String qNumStr = key.replace("question_", "").replace("[]", "");
                int qNum = Integer.parseInt(qNumStr);

                // For multiple choice, values is array; for single it's single value
                List<String> selected = Arrays.asList(values);
                if (selected.size() == 1 && selected.get(0).isEmpty()) {
                    selected = Collections.emptyList(); // no selection
                }
                userAnswers.put(qNum, selected);
            }
        });

        return userAnswers;
    }

}

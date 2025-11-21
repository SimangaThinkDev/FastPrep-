package com.fastprep.site;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.fastprep.site.utils.Tools.*;

@Service
public class ApiController {

    private static final String BASE_PATH = "data_scraping/exams";
    private static final String PractitionerExams = BASE_PATH + "/AWS Certified Cloud Practitioner/";
    private static final List listOfExams;

    static {
        try {
            listOfExams = Files.list( Path.of( BASE_PATH ) ).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helps us get the exam data for each exam
     *
     * @param id The exam id that the user chose
     * @return The exam details as json
     */
    public static JSONObject getExam(int id) {
        toggleActiveController("Get Exam -- API Level");

        try {
            String filename = "practice-exam-" + id + ".json";
            Path path = Path.of(PractitionerExams + filename);

            String json = Files.readString(path);

            ObjectMapper mapper = new ObjectMapper();

            // Jackson reads JSON into a Map. Wrap it in JSONObject.
            Map<String, Object> map = mapper.readValue(json, Map.class);

            return new JSONObject(map);

        }  catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Exam not found", e
            );
        }
    }

}

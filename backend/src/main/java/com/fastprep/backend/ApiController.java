package com.fastprep.backend;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.fastprep.backend.utils.DebugTools.*;

@RestController
@RequestMapping("api/exams")
public class ApiController {

    private static final String BASE_PATH = "data_scraping/exams_data_as_json/";

    /**
     * Helps us get the exam data for each exam
     *
     * @param id The exam id that the user chose
     * @return The exam details as json
     * @throws IOException if parsing the json goes wrong
     */
    @GetMapping("/{id}")
    public Object getExam(@PathVariable int id) throws IOException {
        toggleActiveController( "Get Exam -- API Level" );

        try {
            String filename = "practice-exam-" + id + ".json";
            Path path = Path.of("data_scraping/exams_data_as_json/" + filename);

            String json = Files.readString(path);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Object.class);
        } catch (Exception e) {
            return Map.of("error", "Exam not found");
        }

    }

}

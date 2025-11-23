package com.fastprep.site.persistence;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fastprep.site.service.Ansi.yellow;
import static com.fastprep.site.service.Tools.*;

@Service
public class ExamFileOperations {

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


    /**
     * Displays active controller in a neat manner,
     * can be useful for debugging
     *
     * @param controllerName The name of the controller being used
     */
    public static void toggleActiveController( String controllerName ) {
        System.out.println(
                yellow( "[ Active Controller ] : " + controllerName )
        );
    }

    /**
     * Converts given path to a string
     *
     * @param path the part object to be converted to a string
     * @return the path object as a string
     */
    private static String formatPath( Path path ) {
        return path.toString();
    }

    /**
     * Since the path string is always th whole path from where it starts.
     * And what we want is only the exam name going forwards, we can adapt
     * by taking the last portion of the path, which is where the exam name
     * should be kept by the filekeepers
     *
     * @param pathString the full extracted path of the exam
     * @return the exam name...
     */
    private static String getExamFromPath( String pathString ) {
        List<String> splitPath = List.of(pathString.split("/"));
        return splitPath.getLast(); // Where the exam Name is probably kept...
    }

    /**
     * Gets all the available exams from a list of exam paths
     * Supported by @getExamFromPath and @formatPath
     *
     * @param allCoursePaths A list of all the paths as listed with listDir
     * @return all exam names in a list of strings
     */
    public static List<String> getAllExamsFromPaths( List<Path> allCoursePaths ) {

        List<String> allCourses = new ArrayList<>();

        for ( Path path : allCoursePaths ) {
            String pathString = formatPath( path );
            String exam = getExamFromPath( pathString );

            allCourses.add( exam );
        }

        return allCourses;
    }

    /**
     * Essentially gives us the courses that are currently
     * available in the db/filesystem
     *
     * @return The list of courses as Path Objects
     */
    public static List<Path> getAllCoursePaths() {
        try {
            return Files.list(Path.of(BASE_PATH))
                    .filter(Files::isDirectory)
                    .toList();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Exam directory not found or cannot be read",
                    e
            );
        }
    }

    /**
     * Gets all the exams that are stored for a course
     *
     * @param courseName The name of the course that is being accessed
     * @return the list of exams as path that exist in the course
     */
    public static List<Path> getAllExamsInCourse( String courseName ) {
        String EXAM_PATH = BASE_PATH + "/" + courseName;
        try {
            return Files.list(Path.of(EXAM_PATH)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

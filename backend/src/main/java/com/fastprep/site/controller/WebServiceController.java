package com.fastprep.site.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.fastprep.site.persistence.ExamFileOperations.*;
import static com.fastprep.site.service.GradingTools.gradeAll;
import static com.fastprep.site.service.Tools.*;

@Controller
@RequestMapping( "" )
public class WebServiceController {

    private static final String BASE_PATH = "data_scraping/exams";

    /**
     * This is where we can expose our home page and introduce users to the
     * exams site.
     *
     * Important Goals of this view
     *  1. Display all exam options as hyperlink -- this will now be served in the exams center
     *  2. Handle redirects to the actual exams page
     *  3. Provides users with the ultimate exp.
     *
     * @return The home page of the api
     */
    @GetMapping("/")
    public String index( Model model ) {
        return "index";
    }

    /**
     * The candy store for enthusiastic cloud developers
     * We can start showing users different types of exams
     * for where they want to practice here.
     *
     * @param model attribute holder for frontend
     * @return rendering of the exams.html page
     */
    @GetMapping("/exams")
    public String examsPage(Model model) {
        toggleActiveController("Exams Page");

        List<Path> allCoursePaths = getAllCoursePaths();

        model.addAttribute("allCertificationAvailable", getAllExamsFromPaths(allCoursePaths));
        model.addAttribute("numAvailableCourses", allCoursePaths.size());

        return "exams";
    }

    /**
     * Handles requests for the specific exam details page.
     * Catches the exam name passed as a URL parameter.
     *
     * @param examName The name of the course/exam selected, e.g.,
     * "AWS Certified Solutions Architect"
     * @param model Attribute holder for the frontend
     * @return rendering of the exam_details.html page
     */
    @GetMapping( "/exam/details" )
    public String examDetailsPage( @RequestParam("name") String examName, Model model ) {
        // Open the exam folder of the requested exam
        List<Path> exams = getAllExamsInCourse( examName );
        int numExams = exams.size();

        model.addAttribute( "courseTitle", examName );
        model.addAttribute( "numExams", numExams );
        model.addAttribute( "exams", exams );

        return "exam_details"; // Consider renaming
    }

    /**
     * Handles the request to start an exam for a given course and exam number.
     * <p>
     * Loads the corresponding exam JSON file, extracts metadata (such as title and exam ID),
     * retrieves all related questions, and populates the model with these values for rendering
     * the Thymeleaf exam page.
     *
     * @param examNumber the specific exam number to load
     * @param model      the Spring MVC model used to pass exam data to the view
     * @return the name of the Thymeleaf template for the exam page
     */
    @GetMapping("/exam/start")
    public String startExam(@RequestParam("examNum") int examNumber,
                            Model model) {
        // Logic to load specific questions for courseName and examNumber
        JSONObject examData = getExam( examNumber );

        JSONObject examMetadata = (JSONObject) examData.get( "exam_metadata" );

        // Get the exam title
        String title = examMetadata.getString("title");
        int examID = ((Number) examMetadata.get("exam_id")).intValue();
        JSONArray questions = (JSONArray) examData.get( "questions" );

        model.addAttribute( "title", title );
        model.addAttribute( "examID", examID );
        model.addAttribute( "questionsArray", questions );

        return "exam_page";
    }

    /**
     * Handles the submission of an exam attempt.
     * <p>
     * Extracts the user's selected answers from the HTTP request, loads the
     * corresponding exam definition, grades the submission, and returns the
     * result page populated with the user's final score. Additional metadata
     * (such as exam title or total questions) may also be added to the model
     * in the future.
     *
     * @param examID  the identifier of the exam being submitted
     * @param request the HTTP request containing the user's answer selections
     * @param model   the Spring MVC model used to pass grading results to the view
     * @return the name of the Thymeleaf template that displays the exam results
     */
    @PostMapping("/exam/submit")
    public String submitExam(@RequestParam("examID") int examID,
                             HttpServletRequest request,
                             Model model) {
        Map<Integer, List<String>> userAnswers = extractUserData( request );
        JSONObject exam = getExam( examID );
        JSONArray questions = exam.getJSONArray( "questions" );

        // Do some Grading...
        int score = gradeAll( userAnswers, questions );

        // TODO: Grade the exam, save results to DB, etc. BACKLOGGED
        model.addAttribute( "score", score );
        return "exam_result"; // create this page or redirect
    }

}

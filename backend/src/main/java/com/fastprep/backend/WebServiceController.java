package com.fastprep.backend;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.fastprep.backend.ApiController.getExam;
import static com.fastprep.backend.utils.Ansi.*;
import static com.fastprep.backend.utils.DebugTools.*;

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
        toggleActiveController( "Index" );

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
    public String examsPage( Model model ) {
        toggleActiveController( "Exams Page" );

        try {
            // 1. Get the list of paths in one efficient call
            List<Path> allCoursePaths = getAllCoursePaths();

            // 2. Derive both attributes from the single list
            int numExams = allCoursePaths.size();
            List<String> allCourses = getAllExamsFromPaths( allCoursePaths );

            model.addAttribute( "allCertificationAvailable", allCourses );
            model.addAttribute( "numAvailableCourses", numExams );

        } catch (IOException e) {
            // Log the failure instead of throwing a generic RuntimeException
            System.err.println("Failed to read course directories: " + e.getMessage());
            // Return an empty list or an error view state gracefully
            model.addAttribute( "allCertificationAvailable", Collections.emptyList() );
            model.addAttribute( "numAvailableCourses", 0 );
        }

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
        toggleActiveController( "Exam Details Page" );

        // Open the exam folder of the requested exam
        List<Path> exams = getAllExamsInCourse( examName );
        int numExams = exams.size();

        model.addAttribute( "courseTitle", examName );
        model.addAttribute( "numExams", numExams );
        model.addAttribute( "exams", exams );

        return "details";
    }

    @GetMapping("/exam/start")
    public String startExam(@RequestParam("course") String courseName,
                            @RequestParam("examNum") int examNumber,
                            Model model) {
        toggleActiveController( "Start Exam" );
        // Logic to load specific questions for courseName and examNumber

        try {
            JSONObject examData = getExam( examNumber );
            JSONObject examMetadata = (JSONObject) examData.get( "exam_metadata" );
            String title = String.valueOf( examMetadata.get( "title" ) );
            int examID = (int) examMetadata.get( "exam_id" );
            JSONArray questions = (JSONArray) examData.get( "questions" );
            int numQuestions = questions.length();

            model.addAttribute( "title", title );
            model.addAttribute( "examID", examID );
            model.addAttribute( "questionsArray", questions );

            System.out.println( green( "[ Exam Metadata ] : " + examData.get( "exam_metadata" ) ) );

        } catch (IOException e) {
            System.out.println( onRed( "[ Debug ] : " ) + red( "Failed to get JSONData" ) );
            throw new RuntimeException(e);
        }

        // ...
        return "exam_page";
    }

    @PostMapping("/exam/submit")
    public String submitExam(@RequestParam("examID") int examID,
                             HttpServletRequest request,
                             Model model) throws IOException {

        toggleActiveController("Submit Exam");

        Map<Integer, List<String>> userAnswers = new HashMap<>();
        JSONObject exam = getExam( examID );
        JSONArray questions = exam.getJSONArray( "questions" );

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



        // Do some Grading...
        int score = 0;
        int trackQ = 0;
        for ( int qNum : userAnswers.keySet() ) {
            if ( isCorrect( trackQ, userAnswers.get( qNum ), questions ) ) {
                score += 1;
            }
            trackQ++;
        }

        System.out.println(green("[User Answers] => " + userAnswers));


        // TODO: Grade the exam, save results to DB, etc.
        model.addAttribute("message", "Exam submitted successfully!");
        model.addAttribute( "score", score );
        return "exam_result"; // create this page or redirect
    }

    private boolean isCorrect(int questionNumber, List<String> userChoice, JSONArray questions ) {
        System.out.println( onBlue( "[ DEBUG ] " ) + blue( "Answering Question " + questionNumber ) );
        JSONObject question = null;

        try {
            question = questions.getJSONObject(questionNumber);
        } catch (JSONException e) {
            System.out.println( "No questions Beyond This" );
            return false;
        }

        // Let's verify if this is correct
        String correctAnswer = question.get("correct_answer_keys").toString();

        if ( correctAnswer.length() == 1 ) {
            if (correctAnswer.equals(userChoice.getFirst())) {
                System.out.println(green("That was correct!!"));
                System.out.println( green( "Your answer was: " + userChoice ) );
                System.out.println(green(
                                "Correct Answer is " + correctAnswer + ". "
                                        + question.get("correct_answer_text")
                        )
                );
                return true;
            }
        }
        else {
            String[] correctAnswerSplit = correctAnswer.split( "," );
            List<String> correctAnswers = new ArrayList<>();

            for ( String choice : correctAnswerSplit ) {
                choice = choice.strip();
                correctAnswers.add( choice );
            }

            if ( correctAnswers.equals( userChoice ) ) {
                System.out.println(green("That was correct!!"));
                System.out.println( green( "Your answer was: " + userChoice ) );
                System.out.println(green(
                                "Correct Answer is " + correctAnswer + ". "
                                        + question.get("correct_answer_text")
                        )
                );
                return true;
            }
        }

        System.out.println(red("OOPS!, That was wrong..."));
        System.out.println( red( "Your answer was: " + userChoice ) );
        System.out.println(red(
                        "Correct Answer is " + correctAnswer + ". "
                                + question.get("correct_answer_text")
                )
        );
        return false;
    }
}

package com.fastprep.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

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

}

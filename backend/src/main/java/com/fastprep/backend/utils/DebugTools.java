package com.fastprep.backend.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.fastprep.backend.utils.Ansi.*;

public class DebugTools {

    private static final String BASE_PATH = "data_scraping/exams";

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

    public static List<Path> getAllCoursePaths() throws IOException {
        Path dir = Path.of(BASE_PATH);
        // Lists the files once
        return Files.list(dir).toList();
    }

}

package com.fastprep.site.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GradingTools {

    /**
     * Runs all the necessary code to complete the whole grading
     * process
     *
     * @param userAnswers The answers of the user from front-end
     * @param questions The questions as retrieved from the db/filesystem
     * @return the score of the user our of the length of question
     */
    public static int gradeAll( Map<Integer, List<String>> userAnswers,
                                JSONArray questions ) {
        int score = 0;
        int trackQ = 0;
        for ( int qNum : userAnswers.keySet() ) {
            if ( isCorrect( trackQ, userAnswers.get( qNum ), questions ) ) {
                score += 1;
            }
            trackQ++;
        }
        return score;
    }

    /**
     * Checks if specific question is correct
     *
     * @param questionNumber the number of the question currently being checked
     * @param userChoice the choice of the user for that question
     * @param questions the array of questions as retrieved from the db/filesystem
     * @return true is the user answered correct, false otherwise
     */
    private static boolean isCorrect(int questionNumber, List<String> userChoice, JSONArray questions ) {
        JSONObject question = getQuestion( questionNumber, questions );

        // Let's verify if this is correct
        String correctAnswer = question.get("correct_answer_keys").toString();

        if ( correctAnswer.length() == 1 ) {
            return gradeSingleChoiceQuestion( correctAnswer, userChoice );
        }
        else {
            return gradeMultiChoiceQuestion( correctAnswer, userChoice );
        }
    }

    /**
     * Gets question by question number from the questions
     * array
     *
     * @param questionNumber the number of the question being accessed
     * @param questions the list of questions as retrieved from db/filesystem
     * @return the question structured in a json object
     */ // Find out how to use the @see annotation to enhance this part of documentation
    private static JSONObject getQuestion( int questionNumber, JSONArray questions ) {

        JSONObject question;
        try {
            question = questions.getJSONObject(questionNumber);
            return question;
        } catch (JSONException e) {
            throw new RuntimeException(e +
                    " \nQuestion Numbers in user data not matching questions in db" +
                    " \n FORFEITING GRADING");
        }
    }

    /**
     * Grades a single choice question, more direct because we are only
     * comparing equality between two strings
     *
     * @param correctAnswer the correct answer as retrieved from the questions Array
     * @param userChoice the choice that the user has selected
     * @return true if the choice equals the correct answer
     */
    private static boolean gradeSingleChoiceQuestion( String correctAnswer, List<String> userChoice) {
        return correctAnswer.equals(userChoice.getFirst());
    }

    /**
     * Grades a multi choice question, less direct as we have
     * to go through the user choices Array and validate against
     * the correct answer array
     *
     * @param correctAnswer the correct answers as retrieved from the questions Array
     * @param userChoice the choices that the user has selected
     * @return true if the choice equals the correct answer
     */
    private static boolean gradeMultiChoiceQuestion( String correctAnswer, List<String> userChoice) {
        String[] correctAnswerSplit = correctAnswer.split( "," );
        List<String> correctAnswers = new ArrayList<>();

        for ( String choice : correctAnswerSplit ) {
            choice = choice.strip();
            correctAnswers.add( choice );
        }

        return correctAnswers.equals( userChoice );
    }

}

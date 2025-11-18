package com.fastprep.backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static com.fastprep.backend.ApiController.getExam;
import static com.fastprep.backend.utils.Ansi.green;
import static com.fastprep.backend.utils.Ansi.red;

public class TestCode {

    public static void main(String[] args) throws IOException {

        JSONObject exam = getExam( 1 );
        JSONArray questions = exam.getJSONArray( "questions" );

        // System.out.println( green( exam.toString( 2 ) ) );

        // Let's say a user answers Question 2 with the following data
        // D
        String userChoice = "F";
        JSONObject firstQuestion = questions.getJSONObject( 0 );

        // Let's Manually verify if this is correct
        System.out.println( green( firstQuestion.toString( 2 ) ) );

        String correctAnswer = firstQuestion.get( "correct_answer_keys" ).toString();

        if ( correctAnswer.equals( userChoice ) ) {
            System.out.println( green( "That was correct!!" ) );
        }
        else {
            System.out.println( red( "OOPS!, That was wrong..." ) );
            System.out.println( red(
                    "Correct Answer is " + correctAnswer + ". "
                            + firstQuestion.get( "correct_answer_text" )
                    )
            );
        }

    }

}

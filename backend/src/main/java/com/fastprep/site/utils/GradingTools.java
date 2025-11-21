package com.fastprep.site.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fastprep.site.utils.Ansi.*;
import static com.fastprep.site.utils.Ansi.green;
import static com.fastprep.site.utils.Ansi.red;

public class GradingTools {

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

    private static boolean isCorrect(int questionNumber, List<String> userChoice, JSONArray questions ) {
        System.out.println( onBlue( "[ DEBUG ] " ) + blue( "Answering Question " + questionNumber ) );
        JSONObject question = getQuestion( questionNumber, questions );

        // Let's verify if this is correct
        String correctAnswer = question.get("correct_answer_keys").toString();

        if ( correctAnswer.length() == 1 ) {
            return gradeSingleChoiceQuestion( correctAnswer, userChoice, question );
        }
        else {
            return gradeMultiChoiceQuestion( correctAnswer, userChoice, question );
        }
    }

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

    private static boolean gradeSingleChoiceQuestion( String correctAnswer, List<String> userChoice, JSONObject question ) {
        return correctAnswer.equals(userChoice.getFirst());
    }

    private static boolean gradeMultiChoiceQuestion( String correctAnswer, List<String> userChoice, JSONObject question ) {
        String[] correctAnswerSplit = correctAnswer.split( "," );
        List<String> correctAnswers = new ArrayList<>();

        for ( String choice : correctAnswerSplit ) {
            choice = choice.strip();
            correctAnswers.add( choice );
        }

        return correctAnswers.equals( userChoice );
    }

}

package com.fastprep.backend.utils;

import static com.fastprep.backend.utils.Ansi.*;

public class DebugTools {

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

}

package com.fastprep.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.fastprep.backend.utils.DebugTools.toggleActiveController;

@Controller
@RequestMapping( "" )
public class WebServiceController {

    /**
     * This is where we can expose our home page and introduce users to the
     * exams site.
     *
     * Important Goals of this view
     *  1. Display all exam options as hyperlink
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

}

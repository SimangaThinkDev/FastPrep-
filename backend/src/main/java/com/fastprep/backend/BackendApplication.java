package com.fastprep.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.fastprep.backend.utils.Ansi.*;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

        // Easy access to homepage
        String url = "http://localhost:8080";
        System.out.println(
                green( "Live on " + hyperlink( url, "" ) )
        );
	}

}

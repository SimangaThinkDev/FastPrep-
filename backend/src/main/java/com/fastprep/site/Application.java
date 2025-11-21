package com.fastprep.site;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.fastprep.site.utils.Ansi.*;

@SpringBootApplication
public class Application {

    @Value("${spring.application.server_url}")
    private static String serverUrl;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

        // Easy access to homepage ! Remove for production environments
        System.out.println(
                green( "Live on " + hyperlink( serverUrl, "" ) )
        );
	}

}

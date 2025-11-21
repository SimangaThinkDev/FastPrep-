package com.fastprep.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Value("${spring.application.server_url}")
    private String serverUrl;

    private static final Logger logger = LoggerFactory.getLogger( Application.class );

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * This serves a simple a straightforward purpose.
     * Show where the server is currently running.
     * Also works in production...
     */
    @Override
    public void run(ApplicationArguments args) {
        logger.info( "\nApplication running on " + serverUrl );
    }

}

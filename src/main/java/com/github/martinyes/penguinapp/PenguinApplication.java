package com.github.martinyes.penguinapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class that serves as the entry point for the application.
 */
@SpringBootApplication
public class PenguinApplication {

    /**
     * The main method of the application. It creates an instance of PenguinApplication
     * and invokes its run method with the provided command-line arguments.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        new PenguinApplication().run(args);
    }

    /**
     * The method to start the application. It uses Spring Boot's SpringApplication
     * to run the application with the specified command-line arguments got from the main method.
     *
     * @param args The command-line arguments passed to the application.
     */
    private void run(String[] args) {
        SpringApplication.run(PenguinApplication.class, args);
    }
}

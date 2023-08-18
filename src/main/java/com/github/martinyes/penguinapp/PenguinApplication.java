package com.github.martinyes.penguinapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PenguinApplication {

    public static void main(String[] args) {
        new PenguinApplication().run(args);
    }

    private void run(String[] args) {
        SpringApplication.run(PenguinApplication.class, args);
    }
}

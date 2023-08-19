package com.github.martinyes.penguinapp.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    private String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    private String dashboard() {
        return "dashboard";
    }

    @GetMapping("auth/login")
    private String login() {
        return "auth/login";
    }

    @GetMapping("auth/registration")
    private String registration() {
        return "auth/registration";
    }
}
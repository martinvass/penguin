package com.github.martinyes.penguinapp.controller;

import com.github.martinyes.penguinapp.server.entity.ServerGroup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    private String index(Model model) {
        model.addAttribute("title", "Penguin - Home");
        return "index";
    }

    @GetMapping("/dashboard")
    private String dashboard(Model model) {
        model.addAttribute("title", "Penguin - Dashboard");
        return "/dashboard/home";
    }

    @GetMapping("/dashboard/groups/{name}")
    private String groupsPage(Model model, @PathVariable String name) {
        ServerGroup group = new ServerGroup(name, "test desc");

        model.addAttribute("group", group);
        return "/dashboard/groups";
    }

    @GetMapping("/features")
    private String features(Model model) {
        model.addAttribute("title", "Penguin - Features");
        return "features";
    }
}
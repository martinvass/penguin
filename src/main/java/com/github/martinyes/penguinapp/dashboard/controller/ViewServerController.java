package com.github.martinyes.penguinapp.dashboard.controller;

import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@AllArgsConstructor
@Controller
public class ViewServerController {

    private final ServerService serverService;

    @GetMapping("/dashboard/server/{serverId}")
    private String dashboardServer(@PathVariable("serverId") Long serverId, Model model) {
        Optional<Server> server = serverService.findById(serverId);

        model.addAttribute("title", "Penguin - Server Details");
        model.addAttribute("server", server.get());

        return "/dashboard/server";
    }
}
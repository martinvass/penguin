package com.github.martinyes.penguinapp.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.data.CreateServerData;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class HomeController {

    private final AppUserService userService;
    private final ServerService serverService;

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

    @PostMapping("/dashboard/server/create")
    private String createServer(Principal principal, @RequestParam("serverName") String serverName,
                                @RequestParam("serverAddr") String serverAddr,
                                @RequestParam("serverDesc") String serverDesc) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        CreateServerData data = new CreateServerData(user.get(), serverAddr, serverName, serverDesc);
        serverService.create(data);
        return "/dashboard/home";
    }

    @GetMapping("/dashboard/groups/{name}")
    private String groupsPage(Model model, @PathVariable String name) {
        //ServerGroup group = new ServerGroup(name, "test desc");

        //model.addAttribute("group", group);
        return "/dashboard/groups";
    }

    @GetMapping("/features")
    private String features(Model model) {
        model.addAttribute("title", "Penguin - Features");
        return "features";
    }
}
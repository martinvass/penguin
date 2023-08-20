package com.github.martinyes.penguinapp.view;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.entity.ServerGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ViewController {

    private final AppUserService userService;

    @Autowired
    public ViewController(AppUserService userService) {
        this.userService = userService;
    }

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

    @GetMapping("/auth/user")
    private String userPage(Model model, Principal principal) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        model.addAttribute("title", "Penguin - Dashboard");
        model.addAttribute("user", user.get());
        return "auth/user";
    }

    @GetMapping("/features")
    private String features(Model model) {
        model.addAttribute("title", "Penguin - Features");
        return "features";
    }

    @GetMapping("auth/login")
    private String login(Model model) {
        model.addAttribute("title", "Penguin - Login");
        return "auth/login";
    }

    @GetMapping("auth/registration")
    private String registration(Model model) {
        model.addAttribute("title", "Penguin - Sign up");
        model.addAttribute("user", new AppUser());

        return "auth/registration";
    }

    @PostMapping("auth/registration")
    private String registration(@ModelAttribute AppUser user) {
        userService.registerUser(user);
        return "auth/login";
    }
}
package com.github.martinyes.penguinapp.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {

    private final AppUserService userService;

    /**
     * Handler method for the admin page.
     *
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view template for the admin page.
     */
    @GetMapping("/admin")
    private String admin(Model model, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            model.addAttribute("title", "Penguin - Admin");

            model.addAttribute("activeUsersCount", userService.getActiveUserCount());
            model.addAttribute("registeredUsersCount", userService.getRegisteredUserCount());

            String currentRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("Unknown");
            model.addAttribute("role", currentRole.toLowerCase());

            return "admin/home";
        }

        return "access-denied";
    }

    @GetMapping("/admin/users")
    private String users(Model model) {
        List<AppUser> users = userService.findAll();

        model.addAttribute("title", "Penguin - Users");
        model.addAttribute("users", users);

        return "admin/users";
    }
}
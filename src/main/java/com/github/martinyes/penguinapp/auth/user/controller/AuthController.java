package com.github.martinyes.penguinapp.auth.user.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.edit.EditData;
import com.github.martinyes.penguinapp.auth.user.exception.UserAlreadyExistsException;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class AuthController {

    private final AppUserService userService;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    @GetMapping("/auth/user")
    private String userPage(Model model, Principal principal) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        model.addAttribute("title", "Penguin - Dashboard");
        model.addAttribute("user", user.get());
        return "auth/user";
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
        try {
            userService.registerUser(user);
        } catch (UserAlreadyExistsException e) {
            return "redirect:/auth/registration?alreadyExists";
        }
        return "redirect:/auth/login?success";
    }

    @PostMapping("auth/user/edit")
    private String edit(Principal principal, @RequestParam("username") String username,
                        @RequestParam("email") String email,
                        HttpServletRequest request, HttpServletResponse response) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        if (username.equals(user.get().getUsername()) && email.equals(user.get().getEmail()))
            return "/auth/user";

        // Invalidate the user's session to log them out
        new SecurityContextLogoutHandler().logout(
                request, response, SecurityContextHolder.getContext().getAuthentication()
        );
        userService.editUser(user.get(), new EditData(username, email));

        return "redirect:/auth/login?userEditSuccess";
    }

    @PostMapping("auth/user/delete")
    private String delete(Principal principal, @RequestParam("passwordDeletion") String password,
                          HttpServletRequest request, HttpServletResponse response) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        // Passwords match, proceed with account deactivation
        if (argon2PasswordEncoder.matches(password, user.get().getPassword())) {
            userService.deleteUser(user.get(), false);

            // Invalidate the user's session to log them out
            new SecurityContextLogoutHandler().logout(
                    request, response, SecurityContextHolder.getContext().getAuthentication()
            );

            return "redirect:/?deletion=success";
        }

        return "redirect:/auth/user?deletion=failure";
    }

    @PostMapping("auth/user/deactivate")
    private String deactivate(Principal principal, @RequestParam("password") String password,
                              HttpServletRequest request, HttpServletResponse response) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        // Passwords match, proceed with account deactivation
        if (argon2PasswordEncoder.matches(password, user.get().getPassword())) {
            userService.deleteUser(user.get(), true);

            // Invalidate the user's session to log them out
            new SecurityContextLogoutHandler().logout(
                    request, response, SecurityContextHolder.getContext().getAuthentication()
            );

            return "redirect:/?deactivation=success";
        }

        return "redirect:/auth/user?deactivation=failure";
    }
}
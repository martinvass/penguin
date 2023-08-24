package com.github.martinyes.penguinapp.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.dto.create.CreateGroupDTO;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditGroupDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.util.RadioFormDeleteOption;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller class for handling requests related to the application's home and dashboard pages.
 */
@Controller
@AllArgsConstructor
public class HomeController {

    private final AppUserService userService;
    private final ServerService serverService;
    private final ServerGroupService serverGroupService;

    /**
     * Handler method for the home page.
     *
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view template for the home page.
     */
    @GetMapping("/")
    private String index(Model model) {
        model.addAttribute("title", "Penguin - Home");
        return "index";
    }

    /**
     * Handler method for the dashboard page.
     *
     * @param model     The model to add attributes for rendering the view.
     * @param principal The authenticated user.
     * @return The name of the view template for the dashboard page.
     * @throws UsernameNotFoundException if the user cannot be found.
     */
    @GetMapping("/dashboard")
    private String dashboard(Model model, Principal principal) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        model.addAttribute("title", "Penguin - Dashboard");
        model.addAttribute("servers", serverService.findByUser(user.get()));
        model.addAttribute("groups", serverGroupService.findByUser(user.get()));
        model.addAttribute("radioForm", new RadioFormDeleteOption());
        model.addAttribute("createServerDTO", new CreateServerDTO());
        model.addAttribute("editServerDTO", new EditServerDTO());
        model.addAttribute("createGroupDTO", new CreateGroupDTO());
        model.addAttribute("editGroupDTO", new EditGroupDTO());
        return "/dashboard/home";
    }

    /**
     * Handler method for the features page.
     *
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view template for the features page.
     */
    @GetMapping("/features")
    private String features(Model model) {
        model.addAttribute("title", "Penguin - Features");
        return "features";
    }
}
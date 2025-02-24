package com.github.martinyes.penguinapp.dashboard.controller;

import com.github.martinyes.penguinapp.auth.repository.AppUserRepository;
import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Controller class for handling administrative functionalities in the application.
 * Provides endpoints for managing users and performing other administrative tasks.
 *
 * <p>This controller is responsible for:
 * <ul>
 *   <li>Handling user management operations, such as deleting, locking or unlocking user accounts.</li>
 *   <li>Processing requests related to administrative views and actions.</li>
 *   <li>Redirecting to appropriate pages based on the outcome of the operations.</li>
 * </ul>
 */
@Controller
@AllArgsConstructor
public class AdminDashboardController {

    private final AppUserService userService;
    private final AppUserRepository userRepository;

    /**
     * Handler method for the admin page.
     *
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view template for the admin page.
     */
    @GetMapping("/admin")
    private String admin(Model model, Authentication authentication) {
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

    /**
     * Handler method for the user management page.
     *
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view template for the user management page.
     */
    @GetMapping("/admin/users")
    private String users(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("title", "Penguin - Users");

        if (page < 1)
            page = 1;

        int pageSize = 15;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<AppUser> userPage = userRepository.findAll(pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        return "admin/users";
    }

    @GetMapping("/admin/configuration")
    private String system(Model model) {
        model.addAttribute("title", "Penguin - Configuration");

        return "admin/configuration";
    }

    /**
     * Handler method for deleting a user account from the admin dashboard.
     *
     * @param id The ID of the user to delete.
     * @return The name of the view template for the user management page.
     */
    @PostMapping("/admin/users/delete")
    private String deleteUser(@RequestParam("userId") Long id) {
        Optional<AppUser> user = userService.findById(id);

        if (user.isEmpty())
            return "redirect:/admin/users?error=userNotFound";

        userService.logoutUser(user.get());
        userService.deleteUser(user.get(), false);

        return "redirect:/admin/users?deletion=success";
    }

    /**
     * Handles the toggling of a user's locked status in the system.
     * This method is mapped to the POST request at "/admin/users/toggleStatus".
     *
     * @param id the ID of the user whose status is to be toggled, provided via the "userIdStatus" request parameter.
     * @return a redirect URL indicating the result of the operation:
     *         - "redirect:/admin/users?error=userNotFound" if the user with the given ID does not exist.
     *         - "redirect:/admin/users?editStatus=locked" if the user's status was successfully changed to locked.
     *         - "redirect:/admin/users?editStatus=unlocked" if the user's status was successfully changed to unlocked.
     */
    @PostMapping("/admin/users/toggleStatus")
    private String toggleStatus(@RequestParam("userIdStatus") Long id) {
        Optional<AppUser> user = userService.findById(id);

        if (user.isEmpty())
            return "redirect:/admin/users?error=userNotFound";

        boolean locked = userService.toggleUserLockedStatus(user.get());
        if (locked)
            return "redirect:/admin/users?editStatus=locked";

        return "redirect:/admin/users?editStatus=unlocked";
    }
}
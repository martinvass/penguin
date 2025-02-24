package com.github.martinyes.penguinapp.server.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.server.exception.ServerGroupNotFoundException;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller class for managing server-related operations in the dashboard.
 */
@AllArgsConstructor
@Controller
public class ServerController {

    private final AppUserService userService;
    private final ServerService serverService;

    /**
     * Handles the creation of a new server.
     *
     * @param principal      The authenticated user.
     * @param dto            The CreateServerDTO containing server creation data.
     * @return A redirect URL to the dashboard's server section.
     * @throws UsernameNotFoundException if the user cannot be found.
     */
    @PostMapping("/dashboard/server/create")
    private String createServer(Principal principal, @ModelAttribute("createServerDTO") CreateServerDTO dto) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        serverService.create(user.get(), dto);
        return "redirect:/dashboard?create=success#servers";
    }

    /**
     * Handles the deletion of a server.
     *
     * @param id      The ID of the server to be deleted.
     * @return A redirect URL to the dashboard's server section.
     */
    @PostMapping("/dashboard/server/delete")
    private String deleteServer(@RequestParam("serverId") Long id) {
        serverService.delete(id);
        return "redirect:/dashboard?delete=success#servers";
    }

    /**
     * Handles the editing of an existing server.
     *
     * @param id  The ID of the server to be edited.
     * @param dto The EditServerDTO containing server edit data.
     * @return A redirect URL to the dashboard's server section.
     */
    @PostMapping("/dashboard/server/edit")
    private String editServer(@RequestParam("serverEditId") Long id, @ModelAttribute("editServerDTO") EditServerDTO dto) {
        EditServerDTO editData = new EditServerDTO(
                dto.getServerName(),
                dto.getServerAddr(),
                dto.getServerDesc(),
                dto.getServerGroupName()
        );

        try {
            serverService.edit(id, editData);
        } catch (ServerGroupNotFoundException e) {
            return "redirect:/dashboard?edit=failure#servers";
        }
        return "redirect:/dashboard?edit=success#servers";
    }
}
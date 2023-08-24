package com.github.martinyes.penguinapp.server.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.edit.EditGroupDTO;
import com.github.martinyes.penguinapp.util.DeleteOption;
import com.github.martinyes.penguinapp.util.RadioFormDeleteOption;
import com.github.martinyes.penguinapp.server.dto.create.CreateGroupDTO;
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
 * Controller class for managing server group related operations in the dashboard.
 */
@AllArgsConstructor
@Controller
public class ServerGroupController {

    private final AppUserService userService;
    private final ServerService serverService;
    private final ServerGroupService serverGroupService;

    /**
     * Handles the creation of a new group.
     *
     * @param principal      The authenticated user.
     * @param dto            The CreateGroupDTO containing server creation data.
     * @return A redirect URL to the dashboard's groups section.
     * @throws UsernameNotFoundException if the user cannot be found.
     */
    @PostMapping("/dashboard/group/create")
    private String createGroup(Principal principal, @ModelAttribute("createGroupDTO") CreateGroupDTO dto) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        serverGroupService.create(user.get(), dto);
        return "redirect:/dashboard#groups";
    }

    /**
     * Deletes a group based on the provided group ID and deletion options.
     *
     * @param id         The ID of the group to be deleted.
     * @param radioForm  An object containing deletion options.
     * @return A redirect URL to the dashboard's groups section.
     */
    @PostMapping("/dashboard/group/delete")
    private String deleteGroup(@RequestParam("groupId") Long id, @ModelAttribute("radioForm") RadioFormDeleteOption radioForm) {
        DeleteOption deleteOption = radioForm.getDeleteOption();
        boolean deleteServers = deleteOption == DeleteOption.YES;

        serverGroupService.delete(id, deleteServers);
        return "redirect:/dashboard#groups";
    }

    /**
     * Edits a group based on the provided group ID and group data.
     *
     * @param id          The ID of the group to be edited.
     * @param dto An object containing the edited group data.
     * @return A redirect URL to the dashboard's groups section.
     */
    @PostMapping("/dashboard/group/edit")
    private String editGroup(@RequestParam("groupEditId") Long id, @ModelAttribute("editGroupDTO") EditGroupDTO dto) {
        serverGroupService.edit(id, dto);
        return "redirect:/dashboard#groups";
    }

    /**
     * Adds a server to a group based on the provided group ID and server name.
     *
     * @param groupId    The ID of the group to which the server will be added.
     * @param serverName The name of the server to be added.
     * @return A redirect URL to a group page.
     */
    @PostMapping("/dashboard/group/add")
    private String addServerToGroup(@RequestParam("groupId") Long groupId, @RequestParam("serverName") String serverName) {
        ServerGroup group = serverGroupService.get(groupId);
        Optional<Server> server = serverService.findByName(serverName);

        if (server.isEmpty())
            throw new RuntimeException("server not found");

        serverGroupService.addServerToGroup(group, server.get());

        return String.format("redirect:/dashboard/groups/%s", group.getName());
    }

    /**
     * Removes a server from a group based on the provided server ID.
     *
     * @param serverId    The ID of the server to which the server will be removed.
     * @return A redirect URL to a group page.
     */
    @PostMapping("/dashboard/group/remove")
    private String removeServerFromGroup(@RequestParam("serverId") Long serverId) {
        Server server = serverService.get(serverId);
        ServerGroup group = server.getServerGroup();

        serverGroupService.removeServerFromGroup(server);
        return String.format("redirect:/dashboard/groups/%s", group.getName());
    }

    /**
     * Handles requests to display the dashboard page for a specific group.
     *
     * @param principal     The authenticated user.
     * @param model     The model used to pass data to the view.
     * @param name      The name of the group for which the dashboard page is being displayed.
     * @return The view name for the group's dashboard page.
     */
    @GetMapping("/dashboard/groups/{name}")
    private String groupsPage(Principal principal, Model model, @PathVariable String name) {
        Optional<ServerGroup> group = serverGroupService.findByName(name);

        if (group.isEmpty())
            throw new RuntimeException("group not found");

        model.addAttribute("group", group.get());
        model.addAttribute("servers", serverService.findByUser(
                userService.findByUsername(principal.getName()).orElseThrow()
        ));
        return "/dashboard/groups";
    }
}
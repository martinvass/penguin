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

@AllArgsConstructor
@Controller
public class ServerGroupController {

    private final AppUserService userService;
    private final ServerService serverService;
    private final ServerGroupService serverGroupService;

    @PostMapping("/dashboard/group/create")
    private String createGroup(Principal principal, @ModelAttribute("createGroupDTO") CreateGroupDTO dto) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        serverGroupService.create(user.get(), dto);
        return "redirect:/dashboard#groups";
    }

    @PostMapping("/dashboard/group/delete")
    private String deleteGroup(@RequestParam("groupId") Long id, @ModelAttribute("radioForm") RadioFormDeleteOption radioForm) {
        DeleteOption deleteOption = radioForm.getDeleteOption();
        boolean deleteServers = deleteOption == DeleteOption.YES;

        serverGroupService.delete(id, deleteServers);
        return "redirect:/dashboard#groups";
    }

    @PostMapping("/dashboard/group/edit")
    private String editGroup(@RequestParam("groupEditId") Long id, @ModelAttribute("editGroupDTO") EditGroupDTO dto) {
        serverGroupService.edit(id, dto);
        return "redirect:/dashboard#groups";
    }

    @PostMapping("/dashboard/group/add")
    private String addServerToGroup(@RequestParam("groupId") Long groupId, @RequestParam("serverName") String serverName) {
        ServerGroup group = serverGroupService.get(groupId);
        Optional<Server> server = serverService.findByName(serverName);

        if (server.isEmpty())
            throw new RuntimeException("server not found");

        serverGroupService.addServerToGroup(group, server.get());

        return String.format("redirect:/dashboard/groups/%s", group.getName());
    }

    @PostMapping("/dashboard/group/remove")
    private String removeServerFromGroup(@RequestParam("serverId") Long serverId) {
        Server server = serverService.get(serverId);
        ServerGroup group = server.getServerGroup();

        serverGroupService.removeServerFromGroup(server);
        return String.format("redirect:/dashboard/groups/%s", group.getName());
    }

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
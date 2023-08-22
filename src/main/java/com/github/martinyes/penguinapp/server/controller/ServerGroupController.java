package com.github.martinyes.penguinapp.server.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.data.CreateGroupData;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ServerGroupController {

    private final AppUserService userService;
    private final ServerService serverService;
    private final ServerGroupService serverGroupService;

    @PostMapping("/dashboard/group/delete")
    private String deleteGroup(@RequestParam("groupId") Long id/*, @RequestParam("deleteServers") boolean deleteServers*/) {
        serverGroupService.delete(id, true);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/group/create")
    private String createGroup(Principal principal, @RequestParam("groupName") String groupName,
                               @RequestParam("groupDesc") String groupDesc) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        CreateGroupData data = new CreateGroupData(user.get(), groupName, groupDesc);
        serverGroupService.create(data);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/group/add")
    private String addServerToGroup(@RequestParam("groupId") Long groupId, @RequestParam("serverName") String serverName) {
        serverGroupService.addServerToGroup(
                serverGroupService.findById(groupId).orElseThrow(),
                serverService.findByName(serverName).orElseThrow()
        );
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/group/remove")
    private String removeServerFromGroup(@RequestParam("serverId") Long serverId) {
        serverGroupService.removeServerFromGroup(
                serverService.findById(serverId).orElseThrow()
        );
        return "redirect:/dashboard";
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
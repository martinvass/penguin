package com.github.martinyes.penguinapp.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.data.CreateGroupData;
import com.github.martinyes.penguinapp.server.data.CreateServerData;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
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
    private final ServerGroupService serverGroupService;

    @GetMapping("/")
    private String index(Model model) {
        model.addAttribute("title", "Penguin - Home");
        return "index";
    }

    @GetMapping("/dashboard")
    private String dashboard(Model model, Principal principal) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        model.addAttribute("title", "Penguin - Dashboard");
        model.addAttribute("servers", serverService.findByUser(user.get()));
        model.addAttribute("groups", serverGroupService.findByUser(user.get()));
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
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/server/delete")
    private String deleteServer(@RequestParam("serverId") Long id) {
        serverService.delete(id);
        return "redirect:/dashboard";
    }

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

    @GetMapping("/dashboard/groups/{name}")
    private String groupsPage(Model model, @PathVariable String name) {
        System.out.println(name);
        Optional<ServerGroup> group = serverGroupService.findByName(name);

        if (group.isEmpty())
            throw new RuntimeException("group not found");

        model.addAttribute("group", group.get());
        return "/dashboard/groups";
    }

    @GetMapping("/features")
    private String features(Model model) {
        model.addAttribute("title", "Penguin - Features");
        return "features";
    }
}
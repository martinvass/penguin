package com.github.martinyes.penguinapp.server.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.data.create.CreateServerData;
import com.github.martinyes.penguinapp.server.data.edit.EditServerData;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ServerController {

    private final AppUserService userService;
    private final ServerService serverService;
    private final ServerGroupService serverGroupService;

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

    @PostMapping("/dashboard/server/edit")
    private String editServer(@RequestParam("serverEditId") Long id, @RequestParam("serverNameEdit") String serverName,
                              @RequestParam("serverAddrEdit") String serverAddress,
                              @RequestParam("serverDescEdit") String serverDesc,
                              @RequestParam("serverGroupEdit") String serverGroupName) {
        ServerGroup group = null;
        if (!Objects.equals(serverGroupName, "0")) {
            Optional<ServerGroup> serverGroup = serverGroupService.findByName(serverGroupName);

            if (serverGroup.isEmpty())throw new RuntimeException("group not found");

            group = serverGroup.get();
        }

        EditServerData editData = new EditServerData(
                serverName,
                serverAddress,
                serverDesc,
                group
        );

        serverService.edit(id, editData);
        return "redirect:/dashboard";
    }
}

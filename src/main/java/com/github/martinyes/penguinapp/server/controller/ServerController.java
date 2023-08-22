package com.github.martinyes.penguinapp.server.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.data.CreateServerData;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ServerController {

    private final AppUserService userService;
    private final ServerService serverService;

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
}

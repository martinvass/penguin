package com.github.martinyes.penguinapp.server.controller;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private String createServer(Principal principal, @ModelAttribute("createServerDTO") CreateServerDTO dto) {
        Optional<AppUser> user = userService.findByUsername(principal.getName());

        if (user.isEmpty())
            throw new UsernameNotFoundException(principal.getName());

        serverService.create(user.get(), dto);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/server/delete")
    private String deleteServer(@RequestParam("serverId") Long id) {
        serverService.delete(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/server/edit")
    private String editServer(@RequestParam("serverEditId") Long id, @ModelAttribute("editServerDTO") EditServerDTO dto) {
        EditServerDTO editData = new EditServerDTO(
                dto.getServerName(),
                dto.getServerAddr(),
                dto.getServerDesc(),
                dto.getServerGroupName()
        );

        serverService.edit(id, editData);
        return "redirect:/dashboard";
    }
}
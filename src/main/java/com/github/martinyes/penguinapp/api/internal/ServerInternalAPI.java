package com.github.martinyes.penguinapp.api.internal;

import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// TODO: implement internal API key
@RestController
@RequestMapping("/api/internal/servers")
@AllArgsConstructor
public class ServerInternalAPI {

    private final ServerService serverService;

    @GetMapping("/get/status/{serverId}")
    public ResponseEntity<Server.ServerStatus> getServerStatus(@PathVariable Long serverId) {
        Optional<Server> server = serverService.findById(serverId);

        return server.map(value -> ResponseEntity.ok(value.getServerStatus())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
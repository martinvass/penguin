package com.github.martinyes.penguinapp.api.internal;

import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/internal/servers")
public class ServerInternalAPI {

    private final ServerService serverService;

    public ServerInternalAPI(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/get/status/{serverId}")
    public ResponseEntity<Server.ServerStatus> getServerStatus(@PathVariable Long serverId) {
        Optional<Server> server = serverService.findById(serverId);

        return server.map(value -> ResponseEntity.ok(value.getServerStatus())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ping/{serverId}")
    public ResponseEntity<Long> pingServer(@PathVariable Long serverId) {
        Optional<Server> server = serverService.findById(serverId);

        server.ifPresent(value -> serverService.ping(value.getId()));
        return server.map(value -> ResponseEntity.ok(value.getResponseTime())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
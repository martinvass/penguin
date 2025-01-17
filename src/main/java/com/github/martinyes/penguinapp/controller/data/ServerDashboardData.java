package com.github.martinyes.penguinapp.controller.data;

import com.github.martinyes.penguinapp.server.Server;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ServerDashboardData {

    private List<Server> servers;

    public long getServerResponseTime(Server server) {
        return servers.stream()
                .filter(s -> s.getName().equals(server.getName()))
                .findFirst()
                .map(Server::getResponseTime)
                .orElse(-1L);
    }

}
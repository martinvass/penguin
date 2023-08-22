package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.data.CreateGroupData;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ServerGroupServiceImpl implements ServerGroupService {

    private final ServerGroupRepository serverGroupRepository;
    private final ServerService serverService;

    @Override
    public List<ServerGroup> findByUser(AppUser user) {
        return serverGroupRepository.findByUser(user);
    }

    @Override
    public Optional<ServerGroup> findByName(String name) {
        return serverGroupRepository.findByName(name);
    }

    @Override
    public void create(CreateGroupData data) {
        ServerGroup group = new ServerGroup();
        group.setUser(data.getUser());
        group.setName(data.getGroupName());
        group.setDescription(data.getGroupDesc().isEmpty() ? "" : data.getGroupDesc());

        serverGroupRepository.save(group);
    }

    @Override
    public void delete(Long id, boolean deleteServers) {
        Optional<ServerGroup> group = serverGroupRepository.findById(id);
        if (group.isEmpty())
            return;

        if (deleteServers) {
            group.get().getServers().stream().map(Server::getId).forEach(serverService::delete);
        } else {
            group.get().getServers().forEach(server -> {
                server.setServerGroup(null);
                serverService.save(server);
            });
        }

        serverGroupRepository.deleteById(id);
    }
}

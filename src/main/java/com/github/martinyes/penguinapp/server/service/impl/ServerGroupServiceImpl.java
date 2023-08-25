package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateGroupDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditGroupDTO;
import com.github.martinyes.penguinapp.server.exception.ServerGroupNotFoundException;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ServerGroupService} interface.
 * Provides methods for managing server groups.
 */
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
    public Optional<ServerGroup> findById(Long id) {
        return serverGroupRepository.findById(id);
    }

    @Override
    public void create(AppUser user, CreateGroupDTO dto) {
        ServerGroup group = new ServerGroup();
        group.setUser(user);
        group.setName(dto.getGroupName());
        group.setDescription(dto.getGroupDesc().isEmpty() ? "" : dto.getGroupDesc());

        serverGroupRepository.save(group);
    }

    @Override
    public void delete(Long id, boolean deleteServers) {
        Optional<ServerGroup> group = serverGroupRepository.findById(id);
        if (group.isEmpty())
            throw new ServerGroupNotFoundException(String.format("Group not found with id %d", id));

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

    @Override
    public void edit(Long id, EditGroupDTO dto) {
        Optional<ServerGroup> group = serverGroupRepository.findById(id);
        if (group.isEmpty())
            throw new ServerGroupNotFoundException(String.format("Group not found with id %d", id));

        group.get().setName(dto.getGroupName());
        group.get().setDescription(dto.getGroupDesc());
        serverGroupRepository.save(group.get());
    }

    @Override
    public void save(ServerGroup serverGroup) {
        serverGroupRepository.save(serverGroup);
    }

    @Override
    public void addServerToGroup(ServerGroup group, Server server) {
        server.setServerGroup(group);
        serverService.save(server);
    }

    @Override
    public void removeServerFromGroup(Server server) {
        server.setServerGroup(null);
        serverService.save(server);
    }

    @Override
    public ServerGroup get(Long id) {
        Optional<ServerGroup> group = serverGroupRepository.findById(id);

        if (group.isEmpty())
            throw new ServerGroupNotFoundException(String.format("Group not found with id %d", id));

        return group.get();
    }
}

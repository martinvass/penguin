package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ServerServiceImpl implements ServerService {

    private final ServerGroupRepository serverGroupRepository;
    private final ServerRepository serverRepository;

    @Override
    public List<Server> findByUser(AppUser user) {
        return serverRepository.findByUser(user);
    }

    @Override
    public Optional<Server> findById(Long id) {
        return serverRepository.findById(id);
    }

    @Override
    public Optional<Server> findByName(String name) {
        return serverRepository.findByName(name);
    }

    @Override
    public void create(AppUser user, CreateServerDTO data) {
        Server server = new Server();
        server.setUser(user);
        server.setAddress(data.getAddress());
        server.setName(data.getName());
        server.setDescription(data.getDescription().isEmpty() ? "" : data.getDescription());

        serverRepository.save(server);
    }

    @Override
    public void delete(Long id) {
        serverRepository.delete(get(id));
    }

    @Override
    public void edit(Long id, EditServerDTO editServerDTO) {
        Server server = get(id);

        ServerGroup group = null;
        if (!Objects.equals(editServerDTO.getServerGroupName(), "0")) {
            Optional<ServerGroup> serverGroup = serverGroupRepository.findByName(editServerDTO.getServerGroupName());

            if (serverGroup.isEmpty()) throw new RuntimeException("group not found");

            group = serverGroup.get();
        }

        server.setName(editServerDTO.getServerName());
        server.setAddress(editServerDTO.getServerAddr());
        server.setDescription(editServerDTO.getServerDesc());
        if (group != null)
            server.setServerGroup(group);

        serverRepository.save(server);
    }

    @Override
    public void save(Server server) {
        serverRepository.save(server);
    }

    @Override
    public Server get(Long id) {
        Optional<Server> server = findById(id);

        if (server.isEmpty())
            throw new RuntimeException("server not found");

        return server.get();
    }
}
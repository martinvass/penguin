package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.data.CreateServerData;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ServerServiceImpl implements ServerService {

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
    public void create(CreateServerData data) {
        Server server = new Server();
        server.setUser(data.getUser());
        server.setAddress(data.getAddress());
        server.setName(data.getName());
        server.setDescription(data.getDescription().isEmpty() ? "" : data.getDescription());

        serverRepository.save(server);
    }

    @Override
    public void delete(Long id) {
        serverRepository.deleteById(id);
    }

    @Override
    public void save(Server server) {
        serverRepository.save(server);
    }
}
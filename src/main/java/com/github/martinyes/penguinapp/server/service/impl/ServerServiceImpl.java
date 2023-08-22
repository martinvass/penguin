package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.data.CreateServerData;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public List<Server> findByUser(AppUser user) {
        return serverRepository.findByUser(user);
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
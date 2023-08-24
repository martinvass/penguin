package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the {@link ServerService} interface.
 * Provides methods for managing servers.
 */
@AllArgsConstructor
@Service
public class ServerServiceImpl implements ServerService {

    private final ServerGroupRepository serverGroupRepository;
    private final ServerRepository serverRepository;

    /**
     * Finds servers associated with the given user.
     *
     * @param user The user for whom to find servers.
     * @return A list of servers associated with the user.
     */
    @Override
    public List<Server> findByUser(AppUser user) {
        return serverRepository.findByUser(user);
    }

    /**
     * Finds a server by its ID.
     *
     * @param id The ID of the server to find.
     * @return An optional containing the found server, or empty if not found.
     */
    @Override
    public Optional<Server> findById(Long id) {
        return serverRepository.findById(id);
    }

    /**
     * Finds a server by its name.
     *
     * @param name The name of the server to find.
     * @return An optional containing the found server, or empty if not found.
     */
    @Override
    public Optional<Server> findByName(String name) {
        return serverRepository.findByName(name);
    }

    /**
     * Creates a new server.
     *
     * @param user The user who is creating the server.
     * @param createServerDTO The data to create the server with.
     */
    @Override
    public void create(AppUser user, CreateServerDTO createServerDTO) {
        Server server = new Server();
        server.setUser(user);
        server.setAddress(createServerDTO.getAddress());
        server.setName(createServerDTO.getName());
        server.setDescription(createServerDTO.getDescription().isEmpty() ? "" : createServerDTO.getDescription());

        serverRepository.save(server);
    }

    /**
     * Deletes a server by its ID.
     *
     * @param id The ID of the server to delete.
     */
    @Override
    public void delete(Long id) {
        serverRepository.delete(get(id));
    }

    /**
     * Edits an existing server.
     *
     * @param id            The ID of the server to edit.
     * @param editServerDTO The data with which to edit the server.
     */
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

    /**
     * Saves a server.
     *
     * @param server The server to save.
     */
    @Override
    public void save(Server server) {
        serverRepository.save(server);
    }

    /**
     * Gets a server by its ID.
     *
     * @param id The ID of the server to get.
     * @return The found server.
     * @throws RuntimeException if the server is not found.
     */
    @Override
    public Server get(Long id) {
        Optional<Server> server = findById(id);

        if (server.isEmpty())
            throw new RuntimeException("server not found");

        return server.get();
    }
}
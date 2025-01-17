package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.server.exception.ServerGroupNotFoundException;
import com.github.martinyes.penguinapp.server.exception.ServerNotFoundException;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import com.github.martinyes.penguinapp.server.service.ServerService;
import com.github.martinyes.penguinapp.util.AppUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ServerService} interface.
 * Provides methods for managing servers.
 */
@Service
public class ServerServiceImpl implements ServerService {

    private final ServerGroupRepository serverGroupRepository;
    private final ServerRepository serverRepository;
    private final Map<Long, Server> serverCache = new ConcurrentHashMap<>();

    public ServerServiceImpl(ServerGroupRepository serverGroupRepository, ServerRepository serverRepository) {
        this.serverGroupRepository = serverGroupRepository;
        this.serverRepository = serverRepository;

        serverRepository.findAll().forEach(server -> {
            serverCache.put(server.getId(), server);
            ping(server.getId());
        });
    }

    /**
     * Finds servers associated with the given user.
     *
     * @param user The user for whom to find servers.
     * @return A list of servers associated with the user.
     */
    @Override
    public List<Server> findByUser(AppUser user) {
        return serverRepository.findByUser(user).stream()
                .peek(server -> {
                    if (serverCache.containsKey(server.getId())) {
                        Server cachedServer = serverCache.get(server.getId());
                        server.setResponseTime(cachedServer.getResponseTime());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Finds a server by its ID.
     *
     * @param id The ID of the server to find.
     * @return An optional containing the found server, or empty if not found.
     */
    @Override
    public Optional<Server> findById(Long id) {
        return serverRepository.findById(id).stream()
                .peek(server -> {
                    if (serverCache.containsKey(server.getId())) {
                        Server cachedServer = serverCache.get(server.getId());
                        server.setResponseTime(cachedServer.getResponseTime());
                    }
                })
                .findFirst();
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
        serverCache.put(server.getId(), server);
    }

    /**
     * Deletes a server by its ID.
     *
     * @param id The ID of the server to delete.
     */
    @Override
    public void delete(Long id) {
        serverRepository.delete(get(id));
        serverCache.remove(id);
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

            if (serverGroup.isEmpty())
                throw new ServerGroupNotFoundException("Group not found");

            group = serverGroup.get();
        }

        server.setName(editServerDTO.getServerName());
        server.setAddress(editServerDTO.getServerAddr());
        server.setDescription(editServerDTO.getServerDesc());
        if (group != null)
            server.setServerGroup(group);

        serverCache.remove(id);
        serverRepository.save(server);
        serverCache.put(id, server);
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
     * Pings a server based on the given serverID.
     *
     * @param serverId The ID of the server to ping.
     * @return The server status given in String after the ping.
     */
    @Override
    public boolean ping(Long serverId) {
        Server server = serverCache.get(serverId);

        if (server == null) {
            throw new IllegalArgumentException("Server not found");
        }

        boolean result = false;
        try {
            if (!AppUtils.isIPv4Address(server.getAddress())) result = AppUtils.performHttpPing(server);
            else result = AppUtils.performSystemPing(server);

            if (result)
                server.setServerStatus(Server.ServerStatus.UP);
            else
                server.setServerStatus(Server.ServerStatus.DOWN);
        } catch (IOException | InterruptedException e) {
            server.setResponseTime(-1L);
            server.setServerStatus(Server.ServerStatus.DOWN);
        }

        serverRepository.save(server);
        serverCache.put(serverId, server);
        return result;
    }

    /**
     * Gets a server by its ID.
     *
     * @param id The ID of the server to get.
     * @return The found server.
     * @throws ServerNotFoundException if the server is not found.
     */
    @Override
    public Server get(Long id) {
        Optional<Server> server = findById(id);

        if (server.isEmpty())
            throw new ServerNotFoundException(String.format("Server not found with id %d", id));

        return server.get();
    }
}
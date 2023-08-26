package com.github.martinyes.penguinapp.server.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;
import com.github.martinyes.penguinapp.server.exception.ServerGroupNotFoundException;
import com.github.martinyes.penguinapp.server.exception.ServerNotFoundException;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.service.ServerService;
import com.github.martinyes.penguinapp.util.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

            if (serverGroup.isEmpty())
                throw new ServerGroupNotFoundException("Group not found");

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
     * Pings a server based on the given serverID.
     *
     * @param serverId The ID of the server to ping.
     * @return The server status given in String after the ping.
     */
    @Override
    public boolean ping(Long serverId) {
        Server server = get(serverId);

        if (!AppUtils.isIPv4Address(server.getAddress())) {
            try {
                URL url = new URL(server.getAddress());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();

                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException e) {
                return false;
            }
        }

        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

            ProcessBuilder processBuilder = new ProcessBuilder("ping",
                    isWindows? "-n" : "-c", "1", server.getAddress());
            Process proc = processBuilder.start();
            return proc.waitFor(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return false;
        }
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

    @Scheduled(fixedRate = 10000)
    public void updateServerStatus() {
        List<Server> servers = serverRepository.findAll();
        for (Server server : servers) {
            boolean res = ping(server.getId());

            server.setServerStatus(res ? Server.ServerStatus.UP : Server.ServerStatus.DOWN);
            serverRepository.save(server);
        }
    }
}
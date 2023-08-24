package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing servers.
 * Defines methods for interacting with and managing server entities.
 */
public interface ServerService {

    /**
     * Finds servers associated with the given user.
     *
     * @param user The user for whom to find servers.
     * @return A list of servers associated with the user.
     */
    List<Server> findByUser(AppUser user);

    /**
     * Finds a server by its ID.
     *
     * @param id The ID of the server to find.
     * @return An optional containing the found server, or empty if not found.
     */
    Optional<Server> findById(Long id);

    /**
     * Finds a server by its name.
     *
     * @param name The name of the server to find.
     * @return An optional containing the found server, or empty if not found.
     */
    Optional<Server> findByName(String name);

    /**
     * Creates a new server.
     *
     * @param user The user who is creating the server.
     * @param createServerDTO The data to create the server with.
     */
    void create(AppUser user, CreateServerDTO createServerDTO);

    /**
     * Deletes a server by its ID.
     *
     * @param id The ID of the server to delete.
     */
    void delete(Long id);

    /**
     * Edits an existing server.
     *
     * @param id            The ID of the server to edit.
     * @param editServerDTO The data with which to edit the server.
     */
    void edit(Long id, EditServerDTO editServerDTO);

    /**
     * Saves a server.
     *
     * @param server The server to save.
     */
    void save(Server server);

    /**
     * Gets a server by its ID.
     *
     * @param id The ID of the server to get.
     * @return The found server.
     * @throws RuntimeException if the server is not found.
     */
    Server get(Long id);
}
package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateGroupDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing server groups.
 * Defines methods for interacting with and managing server group entities.
 */
public interface ServerGroupService {

    /**
     * Finds server groups associated with the given user.
     *
     * @param user The user for whom to find server groups.
     * @return A list of server groups associated with the user.
     */
    List<ServerGroup> findByUser(AppUser user);

    /**
     * Finds a server group by its ID.
     *
     * @param id The ID of the server group to find.
     * @return An optional containing the found server group, or empty if not found.
     */
    Optional<ServerGroup> findById(Long id);

    /**
     * Finds a server group by its name.
     *
     * @param name The name of the server group to find.
     * @return An optional containing the found server group, or empty if not found.
     */
    Optional<ServerGroup> findByName(String name);

    /**
     * Creates a new server group.
     *
     * @param user           The user who is creating the server group.
     * @param createGroupDTO The data to create the server group with.
     */
    void create(AppUser user, CreateGroupDTO createGroupDTO);

    /**
     * Deletes a server group by its ID.
     *
     * @param id           The ID of the server group to delete.
     * @param deleteServers True if associated servers should be deleted, false otherwise.
     */
    void delete(Long id, boolean deleteServers);

    /**
     * Edits an existing server group.
     *
     * @param id           The ID of the server group to edit.
     * @param editGroupDTO The data with which to edit the server group.
     */
    void edit(Long id, EditGroupDTO editGroupDTO);

    /**
     * Saves a server group.
     *
     * @param group The server group to save.
     */
    void save(ServerGroup group);

    /**
     * Gets a server group by its ID.
     *
     * @param id The ID of the server group to get.
     * @return The found server group.
     * @throws com.github.martinyes.penguinapp.server.exception.ServerGroupNotFoundException if the server group is not found.
     */
    ServerGroup get(Long id);

    /**
     * Adds a server to a server group.
     *
     * @param group  The server group to which to add the server.
     * @param server The server to add to the group.
     */
    void addServerToGroup(ServerGroup group, Server server);

    /**
     * Removes a server from its associated server group.
     *
     * @param server The server to remove from its group.
     */
    void removeServerFromGroup(Server server);
}
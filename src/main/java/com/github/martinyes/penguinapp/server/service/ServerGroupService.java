package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.dto.create.CreateGroupDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditGroupDTO;

import java.util.List;
import java.util.Optional;

public interface ServerGroupService {

    List<ServerGroup> findByUser(AppUser user);
    Optional<ServerGroup> findById(Long id);
    Optional<ServerGroup> findByName(String name);

    void create(AppUser user, CreateGroupDTO createGroupDTO);
    void delete(Long id, boolean deleteServers);
    void edit(Long id, EditGroupDTO editGroupDTO);
    void save(ServerGroup group);
    ServerGroup get(Long id);

    void addServerToGroup(ServerGroup group, Server server);
    void removeServerFromGroup(Server server);
}
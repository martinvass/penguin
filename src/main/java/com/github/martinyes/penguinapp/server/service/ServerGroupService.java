package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.data.CreateGroupData;

import java.util.List;
import java.util.Optional;

public interface ServerGroupService {

    List<ServerGroup> findByUser(AppUser user);
    Optional<ServerGroup> findById(Long id);
    Optional<ServerGroup> findByName(String name);
    void create(CreateGroupData data);
    void delete(Long id, boolean deleteServers);
    void addServerToGroup(ServerGroup group, Server server);
    void removeServerFromGroup(Server server);
}
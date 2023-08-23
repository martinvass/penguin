package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.data.create.CreateServerData;
import com.github.martinyes.penguinapp.server.data.edit.EditServerData;

import java.util.List;
import java.util.Optional;

public interface ServerService {

    List<Server> findByUser(AppUser user);
    Optional<Server> findById(Long id);
    Optional<Server> findByName(String name);
    void create(CreateServerData data);
    void delete(Long id);
    void edit(Long id, EditServerData data);
    void save(Server server);
}
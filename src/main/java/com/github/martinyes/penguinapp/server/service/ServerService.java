package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.dto.create.CreateServerDTO;
import com.github.martinyes.penguinapp.server.dto.edit.EditServerDTO;

import java.util.List;
import java.util.Optional;

public interface ServerService {

    List<Server> findByUser(AppUser user);
    Optional<Server> findById(Long id);
    Optional<Server> findByName(String name);

    void create(AppUser user, CreateServerDTO createServerDTO);
    void delete(Long id);
    void edit(Long id, EditServerDTO editServerDTO);
    void save(Server server);
    Server get(Long id);
}
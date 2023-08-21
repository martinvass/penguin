package com.github.martinyes.penguinapp.server.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.data.CreateServerData;

import java.util.List;

public interface ServerService {

    List<Server> findByUser(AppUser user);
    Server create(CreateServerData data);
}
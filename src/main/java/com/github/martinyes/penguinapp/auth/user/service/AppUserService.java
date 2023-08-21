package com.github.martinyes.penguinapp.auth.user.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.edit.EditData;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AppUserService extends UserDetailsService {

    String registerUser(AppUser user);
    void deleteUser(AppUser user, boolean deactivate);
    void editUser(AppUser user, EditData editData);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
}
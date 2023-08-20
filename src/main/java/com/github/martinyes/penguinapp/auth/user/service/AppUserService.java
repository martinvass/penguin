package com.github.martinyes.penguinapp.auth.user.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AppUserService extends UserDetailsService {

    String registerUser(AppUser user);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
}
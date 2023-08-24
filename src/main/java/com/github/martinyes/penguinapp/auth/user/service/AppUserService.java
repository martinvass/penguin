package com.github.martinyes.penguinapp.auth.user.service;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.dto.EditUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Service interface for managing application users.
 * Defines methods for interacting with and managing {@link AppUser} entities.
 */
public interface AppUserService extends UserDetailsService {

    String registerUser(AppUser user);
    void deleteUser(AppUser user, boolean deactivate);
    void editUser(AppUser user, EditUserDTO editUserDTO);

    /**
     * Finds an application user by their username.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the found AppUser, or an empty Optional if not found.
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Finds an application user by their email.
     *
     * @param email The email of the user to find.
     * @return An Optional containing the found AppUser, or an empty Optional if not found.
     */
    Optional<AppUser> findByEmail(String email);
}
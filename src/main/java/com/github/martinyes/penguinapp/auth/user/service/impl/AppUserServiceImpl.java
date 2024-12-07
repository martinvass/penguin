package com.github.martinyes.penguinapp.auth.user.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.repository.AppUserRepository;
import com.github.martinyes.penguinapp.auth.user.dto.EditUserDTO;
import com.github.martinyes.penguinapp.auth.user.exception.UserAlreadyExistsException;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing application users.
 */
@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with username %s not found";

    private final AppUserRepository appUserRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    private final SessionRegistry sessionRegistry;

    @Override
    public String registerUser(AppUser user) {
        boolean userExistsByEmail = appUserRepository
                .findByEmail(user.getEmail())
                .isPresent();

        boolean userExistsByUsername = appUserRepository
                .findByUsername(user.getUsername())
                .isPresent();

        if (userExistsByEmail || userExistsByUsername)
            throw new UserAlreadyExistsException();

        user.setPassword(argon2PasswordEncoder
                .encode(user.getPassword()));
        appUserRepository.save(user);

        return "";
    }

    /**
     * Deletes or deactivates an application user based on the provided flag.
     *
     * @param user       The AppUser to be deleted or deactivated.
     * @param deactivate Flag indicating whether to deactivate the user (true) or delete them (false).
     */
    @Override
    public void deleteUser(AppUser user, boolean deactivate) {
        if (deactivate) {
            user.setEnabled(false);
            appUserRepository.save(user);
            return;
        }

        appUserRepository.delete(user);
    }

    /**
     * Edits an existing application user's information based on the provided DTO.
     *
     * @param user The AppUser to be edited.
     * @param dto  The EditUserDTO containing the updated user information.
     */
    @Override
    public void editUser(AppUser user, EditUserDTO dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        appUserRepository.save(user);
    }

    /**
     * Finds an application user by their username.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the found AppUser, or an empty Optional if not found.
     */
    @Override
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    /**
     * Finds an application user by their email.
     *
     * @param email The email of the user to find.
     * @return An Optional containing the found AppUser, or an empty Optional if not found.
     */
    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, username)));
    }

    @Override
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Override
    public int getActiveUserCount() {
        return sessionRegistry.getAllPrincipals().size();
    }

    @Override
    public long getRegisteredUserCount() {
        return appUserRepository.count();
    }
}
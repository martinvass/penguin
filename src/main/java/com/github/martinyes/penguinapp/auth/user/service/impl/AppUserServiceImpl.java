package com.github.martinyes.penguinapp.auth.user.service.impl;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.repository.AppUserRepository;
import com.github.martinyes.penguinapp.auth.user.dto.EditUserDTO;
import com.github.martinyes.penguinapp.auth.user.exception.UserAlreadyExistsException;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with username %s not found";

    private final AppUserRepository appUserRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    // TODO: token implementation
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

    @Override
    public void deleteUser(AppUser user, boolean deactivate) {
        if (deactivate) {
            user.setEnabled(false);
            appUserRepository.save(user);
            return;
        }

        appUserRepository.delete(user);
    }

    @Override
    public void editUser(AppUser user, EditUserDTO dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        appUserRepository.save(user);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

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
}
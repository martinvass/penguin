package com.github.martinyes.penguinapp.auth.user.service.impl;

import com.github.martinyes.penguinapp.auth.repository.AppUserRepository;
import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.dto.EditUserDTO;
import com.github.martinyes.penguinapp.auth.user.exception.UserAlreadyExistsException;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.service.ServerGroupService;
import com.github.martinyes.penguinapp.server.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
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

    private final ServerGroupService serverGroupService;
    private final ServerService serverService;

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

        if (!user.getServerGroups().isEmpty())
            user.getServerGroups().forEach(serverGroup -> serverGroupService.delete(serverGroup.getId(), true));
        else
            user.getServers().forEach(server -> serverService.delete(server.getId()));
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

    /**
     * Finds an application user by their ID.
     *
     * @param id The ID of the user to find.
     * @return An Optional containing the found AppUser, or an empty Optional if not found.
     */
    @Override
    public Optional<AppUser> findById(long id) {
        return appUserRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, username)));
    }

    /**
     * Retrieves a list of all {@link AppUser} entities from the database.
     *
     * @return a {@link List} containing all {@link AppUser} objects.
     *         If no users are found, the list will be empty.
     */
    @Override
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    /**
     * Retrieves the count of currently active users in the system.
     * <p>
     * This method uses the {@code SessionRegistry} to determine the number of
     * unique principals (users) currently authenticated and active in the application.
     *
     * @return the number of active users.
     */
    @Override
    public int getActiveUserCount() {
        return sessionRegistry.getAllPrincipals().size();
    }

    /**
     * Retrieves the total number of registered users in the system.
     * <p>
     * This method queries the {@code AppUserRepository} to get the count of all
     * persisted user entities in the database.
     *
     * @return the total count of registered users.
     */
    @Override
    public long getRegisteredUserCount() {
        return appUserRepository.count();
    }

    /**
     * Logs out a specific user by invalidating all their active sessions.
     * <p>
     * This method iterates through all active principals in the {@code SessionRegistry}.
     * If a principal matching the provided user's username is found, all their sessions
     * are retrieved and marked as expired. This effectively logs out the user by
     * invalidating their session data.
     *
     * @param user the {@code AppUser} to be logged out.
     *             This user is identified by their username.
     */
    @Override
    public void logoutUser(AppUser user) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        String username = user.getUsername();

        for (Object principal : allPrincipals) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
                    for (SessionInformation session : sessions)
                        session.expireNow();

                    return;
                }
            }
        }
    }

    @Override
    public boolean toggleUserLockedStatus(AppUser user) {
        user.setLocked(!user.getLocked());
        appUserRepository.save(user);

        return user.getLocked();
    }
}
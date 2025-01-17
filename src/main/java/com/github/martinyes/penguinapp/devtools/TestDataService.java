package com.github.martinyes.penguinapp.devtools;

import com.github.martinyes.penguinapp.auth.repository.AppUserRepository;
import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.auth.user.AppUserRole;
import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TestDataService {

    private final AppUserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ServerGroupRepository serverGroupRepository;
    private final AppUserService userService;

    public void generateTestData(int userCount, int serversPerUser) {
        for (int i = 1; i <= userCount; i++) {
            String uniqueUsername = UUID.randomUUID().toString();
            String uniqueEmail = uniqueUsername.substring(0, 5) + "@test.com";

            AppUser user = new AppUser();
            user.setUsername(uniqueUsername);
            user.setEmail(uniqueEmail);
            user.setPassword("password" + i);
            user.setRole(AppUserRole.TEST);
            userRepository.save(user);

            for (int j = 1; j <= serversPerUser; j++) {
                Server server = new Server();
                server.setAddress(String.format("http://localhost:%d/", i));
                server.setName("Server " + j + " for user " + i);
                server.setDescription("Server " + j + " for user " + i);
                server.setUser(user);
                serverRepository.save(server);

                ServerGroup serverGroup = new ServerGroup();
                serverGroup.setName("Group " + j + " for server " + server.getName());
                serverGroup.getServers().add(server);
                serverGroupRepository.save(serverGroup);
            }
        }
    }

    public void deleteTestData() {
        userRepository.findAll().stream().filter(u -> u.getRole().equals(AppUserRole.TEST)).forEach(u -> userService.deleteUser(u, false));
    }
}

package com.github.martinyes.penguinapp.devtools;

import com.github.martinyes.penguinapp.auth.repository.AppUserRepository;
import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import com.github.martinyes.penguinapp.server.repository.ServerGroupRepository;
import com.github.martinyes.penguinapp.server.repository.ServerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestDataService {

    private final AppUserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ServerGroupRepository serverGroupRepository;

    public void generateTestData(int userCount, int serversPerUser) {
        for (int i = 1; i <= userCount; i++) {
            AppUser user = new AppUser();
            user.setUsername("testuser" + i);
            user.setEmail("testuser" + i + "@example.com");
            user.setPassword("password" + i);
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
}

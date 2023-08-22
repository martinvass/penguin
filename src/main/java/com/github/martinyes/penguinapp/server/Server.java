package com.github.martinyes.penguinapp.server;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "servers")
public class Server {

    @SequenceGenerator(
            name = "server_sequence",
            sequenceName = "server_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "server_sequence"
    )
    private Long id;

    @NotEmpty(message = "IP address cannot be null")
    private String address;

    @NotEmpty(message = "Server name cannot be null")
    @Column(unique = true)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private ServerStatus serverStatus = ServerStatus.UNKNOWN;

    @ManyToOne
    @JoinColumn(name = "server_group_id")
    private ServerGroup serverGroup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Getter
    @AllArgsConstructor
    public enum ServerStatus {
        UNKNOWN("Unknown"),
        UP("Server Up"),
        DOWN("Server Down");

        private final String name;
    }
}
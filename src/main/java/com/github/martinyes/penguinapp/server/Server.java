package com.github.martinyes.penguinapp.server;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * Entity class representing a server.
 */
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
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private ServerStatus serverStatus = ServerStatus.UNKNOWN;

    @Column
    private Date creationDate = new Date(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "server_group_id")
    private ServerGroup serverGroup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Transient
    private long responseTime;

    /**
     * Enum representing the status of a server.
     */
    @Getter
    @AllArgsConstructor
    public enum ServerStatus {

        /**
         * Status indicating the server's status is unknown.
         */
        UNKNOWN("Unknown"),

        /**
         * Status indicating the server is up and running.
         */
        UP("Server Up"),

        /**
         * Status indicating the server is down or not reachable.
         */
        DOWN("Server Down");

        /**
         * The display name of the server status.
         */
        private final String name;
    }
}
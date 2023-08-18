package com.github.martinyes.penguinapp.server.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "IP address cannot be null")
    @Column(unique = true)
    private String address;

    @NotEmpty(message = "Server name cannot be null")
    @Column(unique = true)
    private String name;

    private String description;
    private ServerStatus serverStatus = ServerStatus.UNKNOWN;

    @Getter
    @AllArgsConstructor
    public enum ServerStatus {
        UNKNOWN("Unknown"),
        UP("Server Up"),
        DOWN("Server Down");

        private final String name;
    }
}
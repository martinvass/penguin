package com.github.martinyes.penguinapp.server;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a server group.
 */
@Getter
@Setter
@Entity
public class ServerGroup {

    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;

    @Column
    @NotEmpty(message = "Group name cannot be null")
    private String name;

    @Column
    private String description;

    @Column
    private Date creationDate = new Date(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "serverGroup")
    private List<Server> servers = new ArrayList<>();
}
package com.github.martinyes.penguinapp.auth.user;

import com.github.martinyes.penguinapp.server.Server;
import com.github.martinyes.penguinapp.server.ServerGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class representing an application user implementing Spring Security's UserDetails interface.
 */
@Getter
@Setter
@ToString
@Entity(name = "users")
public class AppUser implements UserDetails {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @NotEmpty(message = "Username cannot be null")
    @Size(min = 5, max = 250)
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Email cannot be null")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password cannot be null")
    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole role = AppUserRole.USER;
    private Boolean locked = false, enabled = true;
    private Date creationDate = new Date(System.currentTimeMillis());

    @OneToMany(mappedBy = "user")
    private List<ServerGroup> serverGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Server> servers = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getAccountID() {
        return "#" + id;
    }
}
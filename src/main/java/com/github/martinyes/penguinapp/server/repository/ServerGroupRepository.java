package com.github.martinyes.penguinapp.server.repository;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.ServerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ServerGroupRepository extends JpaRepository<ServerGroup, Long> {

    List<ServerGroup> findByUser(AppUser user);
    Optional<ServerGroup> findByName(String name);
}

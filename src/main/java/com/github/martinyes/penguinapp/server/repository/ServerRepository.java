package com.github.martinyes.penguinapp.server.repository;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import com.github.martinyes.penguinapp.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findByUser(AppUser user);
}
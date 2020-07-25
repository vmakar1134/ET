package com.eventsterminal.server.repository;

import com.eventsterminal.server.config.model.Role;
import com.eventsterminal.server.config.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);

}

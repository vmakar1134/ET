package com.eventsterminal.server.repository;

import com.eventsterminal.server.config.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findByUsername(String email);

    Optional<UserAuth> findBySocialId(BigInteger socialId);

    boolean existsByUsername(String email);

}

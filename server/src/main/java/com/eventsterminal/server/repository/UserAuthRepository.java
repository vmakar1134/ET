package com.eventsterminal.server.repository;

import com.eventsterminal.server.config.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findBySocialId(@Param("id") BigInteger id);

    Optional<UserAuth> findByEmail(String email);

    boolean existsByEmail(String email);

}

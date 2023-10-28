package com.api.usuario.repositories;

import com.api.usuario.models.SenhaToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SenhaTokenRepository extends JpaRepository<SenhaToken, Long> {

    Optional<SenhaToken> findByToken(String token);
}

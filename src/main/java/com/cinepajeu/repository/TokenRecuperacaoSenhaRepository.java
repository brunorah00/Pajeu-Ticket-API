package com.cinepajeu.repository;

import com.cinepajeu.entity.OAuthProvider;
import com.cinepajeu.entity.TokenRecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRecuperacaoSenhaRepository extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findByTokenAndUsedAtIsNull(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE TokenRecuperacaoSenha t SET t.usedAt = :now WHERE t.usuario.id = :usuarioId AND t.usedAt IS NULL")
    void invalidateActiveTokensForUsuario(@Param("usuarioId") Long usuarioId, @Param("now") LocalDateTime now);
}

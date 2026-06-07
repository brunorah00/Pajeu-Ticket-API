package com.cinepajeu.repository;

import com.cinepajeu.entity.OAuthProvider;
import com.cinepajeu.entity.UserRole;
import com.cinepajeu.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByLoginIgnoreCase(String login);

    Optional<Usuario> findByOauthProviderAndOauthSubject(OAuthProvider oauthProvider, String oauthSubject);

    List<Usuario> findByRole(UserRole role);
}

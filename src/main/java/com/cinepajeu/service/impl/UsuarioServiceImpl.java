package com.cinepajeu.service.impl;

import com.cinepajeu.dto.*;
import com.cinepajeu.entity.UserRole;
import com.cinepajeu.entity.Usuario;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.UsuarioRepository;
import com.cinepajeu.util.LoginNormalizer;
import com.cinepajeu.security.JwtService;
import com.cinepajeu.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getSenha())
            );
        } catch (Exception e) {
            throw new BusinessException("Credenciais inválidas: login ou senha incorretos");
        }

        Usuario usuario = usuarioRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado após autenticação"));

        usuario.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        return LoginResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .login(usuario.getLogin())
                .nome(usuario.getNome())
                .role(usuario.getRole().name())
                .build();
    }

    @Override
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String token = request.getRefreshToken();
        String login;
        try {
            login = jwtService.extractUsername(token);
        } catch (Exception e) {
            throw new BusinessException("Refresh token inválido");
        }

        if (login != null) {
            Usuario usuario = usuarioRepository.findByLoginIgnoreCase(LoginNormalizer.normalize(login))
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado para o token fornecido"));

            if (jwtService.isTokenValid(token, usuario)) {
                String accessToken = jwtService.generateToken(usuario);
                String newRefreshToken = jwtService.generateRefreshToken(usuario);
                return LoginResponseDTO.builder()
                        .token(accessToken)
                        .refreshToken(newRefreshToken)
                        .login(usuario.getLogin())
                        .nome(usuario.getNome())
                        .role(usuario.getRole().name())
                        .build();
            }
        }
        throw new BusinessException("Refresh token expirado ou inválido");
    }

    @Override
    @Transactional
    public void alterarSenha(AlterarSenhaRequestDTO request) {
        String login = LoginNormalizer.normalize(request.getLogin());
        Usuario usuario = usuarioRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenhaAntiga(), usuario.getSenha())) {
            throw new BusinessException("A senha antiga está incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(request.getSenhaNova()));
        usuarioRepository.save(usuario);
    }

    @Override
    public void recuperarSenha(RecuperarSenhaRequestDTO request) {
        String login = LoginNormalizer.normalize(request.getLogin());
        Usuario usuario = usuarioRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        // Estrutura preparada para envio de e-mail / recuperação
        log.info("Recuperação de senha solicitada para o login: {}. Preparado link de reset para o e-mail cadastrado.", usuario.getLogin());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        String login = LoginNormalizer.normalize(request.getLogin());
        if (login == null || login.isEmpty()) {
            throw new BusinessException("Login inválido");
        }

        if (usuarioRepository.findByLoginIgnoreCase(login).isPresent()) {
            throw new BusinessException("Já existe um usuário cadastrado com este login");
        }

        if (request.getRole() != null && request.getRole() != UserRole.CLIENTE) {
            throw new BusinessException("Cadastro público permitido apenas para perfil de cliente");
        }

        Usuario usuario = ModelMapper.toEntity(request);
        usuario.setLogin(login);
        usuario.setRole(UserRole.CLIENTE);
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        Usuario salvo = usuarioRepository.save(usuario);
        return ModelMapper.toDto(salvo);
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com o ID: " + id));
        return ModelMapper.toDto(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarFuncionarios() {
        return usuarioRepository.findByRole(UserRole.FUNCIONARIO).stream()
                .map(ModelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO request) {
        String login = LoginNormalizer.normalize(request.getLogin());
        if (login == null || login.isEmpty()) {
            throw new BusinessException("Login inválido");
        }

        var existente = usuarioRepository.findByLoginIgnoreCase(login);
        if (existente.isPresent()) {
            Usuario usuario = existente.get();
            if (usuario.getRole() == UserRole.CLIENTE) {
                usuario.setNome(request.getNome().trim());
                usuario.setRole(UserRole.FUNCIONARIO);
                usuario.setSenha(passwordEncoder.encode(request.getSenha()));
                return ModelMapper.toDto(usuarioRepository.save(usuario));
            }
            throw new BusinessException("Já existe um usuário cadastrado com este login");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome().trim())
                .login(login)
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(UserRole.FUNCIONARIO)
                .build();

        return ModelMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void redefinirSenhaFuncionario(Long id, RedefinirSenhaFuncionarioDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Funcionário não encontrado"));

        if (usuario.getRole() != UserRole.FUNCIONARIO) {
            throw new BusinessException("Apenas funcionários podem ter a senha redefinida por esta operação");
        }

        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void excluirFuncionario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Funcionário não encontrado"));

        if (usuario.getRole() != UserRole.FUNCIONARIO) {
            throw new BusinessException("Apenas funcionários podem ser removidos por esta operação");
        }

        usuarioRepository.delete(usuario);
    }
}

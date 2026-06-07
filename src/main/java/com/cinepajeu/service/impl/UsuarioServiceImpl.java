package com.cinepajeu.service.impl;

import com.cinepajeu.dto.*;
import com.cinepajeu.entity.OAuthProvider;
import com.cinepajeu.entity.TokenRecuperacaoSenha;
import com.cinepajeu.entity.UserRole;
import com.cinepajeu.entity.Usuario;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.TokenRecuperacaoSenhaRepository;
import com.cinepajeu.repository.UsuarioRepository;
import com.cinepajeu.util.EmailValidator;
import com.cinepajeu.util.LoginNormalizer;
import com.cinepajeu.security.JwtService;
import com.cinepajeu.service.EmailService;
import com.cinepajeu.service.OAuthUserInfo;
import com.cinepajeu.service.OAuthVerificationService;
import com.cinepajeu.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OAuthVerificationService oauthVerificationService;

    @Value("${app.frontend.base-url:http://localhost:3000}")
    private String frontendBaseUrl;

    @Value("${app.password-reset.expiration-minutes:15}")
    private int passwordResetExpirationMinutes;

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

        Usuario usuario = usuarioRepository.findByLoginIgnoreCase(LoginNormalizer.normalize(request.getLogin()))
                .orElseThrow(() -> new BusinessException("Usuário não encontrado após autenticação"));

        usuario.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return buildLoginResponse(usuario);
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
    @Transactional
    public void recuperarSenha(RecuperarSenhaRequestDTO request) {
        String login = LoginNormalizer.normalize(request.getLogin());
        if (login == null || login.isBlank()) {
            return;
        }

        if (!EmailValidator.isValid(login)) {
            return;
        }

        usuarioRepository.findByLoginIgnoreCase(login).ifPresent(usuario -> {
            tokenRecuperacaoSenhaRepository.invalidateActiveTokensForUsuario(
                    usuario.getId(), LocalDateTime.now());

            String token = UUID.randomUUID().toString().replace("-", "");
            TokenRecuperacaoSenha reset = TokenRecuperacaoSenha.builder()
                    .token(token)
                    .usuario(usuario)
                    .expiresAt(LocalDateTime.now().plusMinutes(passwordResetExpirationMinutes))
                    .build();
            tokenRecuperacaoSenhaRepository.save(reset);

            String link = frontendBaseUrl.replaceAll("/$", "") + "/redefinir-senha?token=" + token;
            emailService.enviarRecuperacaoSenha(login, usuario.getNome(), link);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ValidarTokenRecuperacaoDTO validarTokenRecuperacao(String token) {
        if (token == null || token.isBlank()) {
            return ValidarTokenRecuperacaoDTO.builder().valido(false).build();
        }

        return tokenRecuperacaoSenhaRepository.findByTokenAndUsedAtIsNull(token.trim())
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(t -> ValidarTokenRecuperacaoDTO.builder()
                        .valido(true)
                        .login(t.getUsuario().getLogin())
                        .build())
                .orElse(ValidarTokenRecuperacaoDTO.builder().valido(false).build());
    }

    @Override
    @Transactional
    public void redefinirSenha(RedefinirSenhaRequestDTO request) {
        TokenRecuperacaoSenha reset = tokenRecuperacaoSenhaRepository
                .findByTokenAndUsedAtIsNull(request.getToken().trim())
                .orElseThrow(() -> new BusinessException("Link inválido ou expirado"));

        if (reset.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Link expirado. Solicite uma nova recuperação de senha.");
        }

        Usuario usuario = reset.getUsuario();
        usuario.setSenha(passwordEncoder.encode(request.getSenhaNova()));
        usuarioRepository.save(usuario);

        reset.setUsedAt(LocalDateTime.now());
        tokenRecuperacaoSenhaRepository.save(reset);
        tokenRecuperacaoSenhaRepository.invalidateActiveTokensForUsuario(
                usuario.getId(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public LoginResponseDTO loginOAuth(OAuthLoginRequestDTO request) {
        OAuthProvider provider;
        try {
            provider = OAuthProvider.valueOf(request.getProvider().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Provedor OAuth inválido");
        }

        OAuthUserInfo info = oauthVerificationService.verify(provider, request.getToken());
        EmailValidator.requireValid(info.email());

        Usuario usuario = usuarioRepository
                .findByOauthProviderAndOauthSubject(provider, info.subject())
                .orElseGet(() -> usuarioRepository.findByLoginIgnoreCase(info.email())
                        .map(existing -> linkOAuthAccount(existing, provider, info))
                        .orElseGet(() -> createOAuthUser(provider, info)));

        if (usuario.getRole() != UserRole.CLIENTE) {
            throw new BusinessException(
                    "Contas de equipe devem entrar com login e senha. Use o acesso do painel.");
        }

        usuario.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return buildLoginResponse(usuario);
    }

    private Usuario linkOAuthAccount(Usuario usuario, OAuthProvider provider, OAuthUserInfo info) {
        if (usuario.getOauthProvider() != null
                && usuario.getOauthSubject() != null
                && (!usuario.getOauthProvider().equals(provider)
                || !usuario.getOauthSubject().equals(info.subject()))) {
            throw new BusinessException("Este e-mail já está vinculado a outra conta social");
        }
        usuario.setOauthProvider(provider);
        usuario.setOauthSubject(info.subject());
        if (info.nome() != null && !info.nome().isBlank()) {
            usuario.setNome(info.nome());
        }
        return usuario;
    }

    private Usuario createOAuthUser(OAuthProvider provider, OAuthUserInfo info) {
        Usuario usuario = Usuario.builder()
                .nome(info.nome())
                .login(info.email())
                .senha(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRole.CLIENTE)
                .oauthProvider(provider)
                .oauthSubject(info.subject())
                .build();
        return usuarioRepository.save(usuario);
    }

    private LoginResponseDTO buildLoginResponse(Usuario usuario) {
        return LoginResponseDTO.builder()
                .token(jwtService.generateToken(usuario))
                .refreshToken(jwtService.generateRefreshToken(usuario))
                .login(usuario.getLogin())
                .nome(usuario.getNome())
                .role(usuario.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        String login = LoginNormalizer.normalize(request.getLogin());
        if (login == null || login.isEmpty()) {
            throw new BusinessException("Login inválido");
        }

        EmailValidator.requireValid(login);

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

package com.cinepajeu.controller;

import com.cinepajeu.dto.*;
import com.cinepajeu.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro, login, refresh token e gerenciamento de senhas")
public class AuthenticationController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login para obter tokens de acesso")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Obter novo Access Token através do Refresh Token")
    public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        LoginResponseDTO response = usuarioService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Efetuar logout (invalidação simulada do token)")
    public ResponseEntity<String> logout() {
        // Como o JWT é stateless, o logout no cliente é feito apagando o token localmente.
        // Respondemos apenas confirmando o logout.
        return ResponseEntity.ok("Logout realizado com sucesso. Remova o token armazenado.");
    }

    @PutMapping("/alterar-senha")
    @Operation(summary = "Alterar a senha do usuário autenticado")
    public ResponseEntity<String> alterarSenha(@Valid @RequestBody AlterarSenhaRequestDTO request) {
        usuarioService.alterarSenha(request);
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }

    @PostMapping("/recuperar-senha")
    @Operation(summary = "Solicitar recuperação de senha por e-mail")
    public ResponseEntity<String> recuperarSenha(@Valid @RequestBody RecuperarSenhaRequestDTO request) {
        usuarioService.recuperarSenha(request);
        return ResponseEntity.ok(
                "Se o e-mail estiver cadastrado, você receberá um link para redefinir a senha em alguns minutos.");
    }

    @GetMapping("/redefinir-senha/validar")
    @Operation(summary = "Validar token de recuperação de senha")
    public ResponseEntity<ValidarTokenRecuperacaoDTO> validarTokenRecuperacao(@RequestParam String token) {
        return ResponseEntity.ok(usuarioService.validarTokenRecuperacao(token));
    }

    @PostMapping("/redefinir-senha")
    @Operation(summary = "Redefinir senha com token recebido por e-mail")
    public ResponseEntity<String> redefinirSenha(@Valid @RequestBody RedefinirSenhaRequestDTO request) {
        usuarioService.redefinirSenha(request);
        return ResponseEntity.ok("Senha redefinida com sucesso. Faça login com a nova senha.");
    }

    @PostMapping("/oauth")
    @Operation(summary = "Login com Google ou Facebook")
    public ResponseEntity<LoginResponseDTO> loginOAuth(@Valid @RequestBody OAuthLoginRequestDTO request) {
        LoginResponseDTO response = usuarioService.loginOAuth(request);
        return ResponseEntity.ok(response);
    }
}

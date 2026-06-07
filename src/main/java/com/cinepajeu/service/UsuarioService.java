package com.cinepajeu.service;

import com.cinepajeu.dto.*;

public interface UsuarioService {
    LoginResponseDTO login(LoginRequestDTO request);
    LoginResponseDTO refreshToken(RefreshTokenRequestDTO request);
    void alterarSenha(AlterarSenhaRequestDTO request);
    void recuperarSenha(RecuperarSenhaRequestDTO request);
    ValidarTokenRecuperacaoDTO validarTokenRecuperacao(String token);
    void redefinirSenha(RedefinirSenhaRequestDTO request);
    LoginResponseDTO loginOAuth(OAuthLoginRequestDTO request);
    UsuarioResponseDTO cadastrar(UsuarioRequestDTO request);
    UsuarioResponseDTO buscarPorId(Long id);
    java.util.List<UsuarioResponseDTO> listarFuncionarios();
    UsuarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO request);
    void excluirFuncionario(Long id);
    void redefinirSenhaFuncionario(Long id, RedefinirSenhaFuncionarioDTO request);
}

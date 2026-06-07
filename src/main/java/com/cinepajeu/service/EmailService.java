package com.cinepajeu.service;

public interface EmailService {
    void enviarRecuperacaoSenha(String destinatario, String nome, String linkRedefinicao);
}

package com.cinepajeu.service.impl;

import com.cinepajeu.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final Optional<JavaMailSender> mailSender;

    @Value("${app.mail.from:noreply@cinesaojose.com}")
    private String fromAddress;

    public EmailServiceImpl(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = Optional.ofNullable(mailSender);
    }

    @Override
    public void enviarRecuperacaoSenha(String destinatario, String nome, String linkRedefinicao) {
        String assunto = "Cine São José — Redefinição de senha";
        String corpo = """
                Olá, %s!

                Recebemos uma solicitação para redefinir a senha da sua conta no Cine São José.

                Acesse o link abaixo (válido por 15 minutos):
                %s

                Se você não solicitou esta alteração, ignore este e-mail.

                Cine São José
                """.formatted(nome, linkRedefinicao);

        if (mailSender.isEmpty()) {
            log.info("SMTP não configurado. Link de recuperação para {}: {}", destinatario, linkRedefinicao);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(corpo);
        mailSender.get().send(message);
        log.info("E-mail de recuperação enviado para {}", destinatario);
    }
}

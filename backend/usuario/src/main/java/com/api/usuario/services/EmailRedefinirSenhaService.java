package com.api.usuario.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailRedefinirSenhaService {

    @Autowired
    JavaMailSender javaMailSender;

    public void enviarEmailRedefinirSenha(String destinatario, String assunto, String nome, String link) throws MessagingException {

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject(assunto);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(MensagemEmailRecuperarSenha.corpoDoEmail(nome, link), true);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            javaMailSender.send(mimeMessage);

        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}

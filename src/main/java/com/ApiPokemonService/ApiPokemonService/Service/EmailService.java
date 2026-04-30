package com.ApiPokemonService.ApiPokemonService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoVerificacion(String to, String token) {

        String link = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verifica tu cuenta");
        message.setText("Haz clic aquí:\n" + link);

        mailSender.send(message);
    }
}

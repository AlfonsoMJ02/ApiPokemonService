package com.ApiPokemonService.ApiPokemonService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoVerificacion(String to, String token) throws MessagingException {

        String link = "http://localhost:8080/auth/verify?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Verifica tu cuenta");

        String contenidoHtml = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin:0; padding:0; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color:#e0e0e0;">

                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#e0e0e0; padding:40px 0;">
                        <tr>
                            <td align="center">

                                <table width="550" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border: 4px solid #333333; border-radius:20px; overflow:hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.2);">

                                    <tr>
                                        <td style="background-color:#ee1515; padding:30px; text-align:center; border-bottom: 12px solid #333333; position: relative;">
                                            <h1 style="margin:0; color:white; text-transform: uppercase; letter-spacing: 2px; text-shadow: 2px 2px #000;">
                                                ¡Nueva Notificación!
                                            </h1>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding:40px; text-align:center; background-image: radial-gradient(#ffffff 70%%, #f9f9f9 100%%);">

                                            <h2 style="color:#2a75bb; font-size: 24px; margin-bottom: 10px;">¡Hola, Entrenador!</h2>

                                            <p style="color:#444; font-size:16px; line-height: 1.6;">
                                                Un <b>Pokémon salvaje</b> ha aparecido en tu bandeja de entrada. <br>
                                                Estás a solo un paso de iniciar tu viaje por la región.
                                                Verifica tu cuenta para registrarte en la Pokédex oficial.
                                            </p>

                                            <a href="%s"
                                               style="display:inline-block; margin-top:25px; padding:15px 35px;
                                                      background-color:#ffcb05; color:#2a75bb;
                                                      text-decoration:none; font-weight:bold; font-size: 18px;
                                                      border: 3px solid #2a75bb; border-radius:50px;
                                                      box-shadow: 0 4px 0 #c7a008;">
                                                ¡YO TE ELIJO! (Verificar)
                                            </a>

                                            <p style="margin-top:40px; font-size:12px; color:#888; font-style: italic;">
                                                "Si no fuiste tú quien lanzó la Pokébola, puedes ignorar este mensaje de forma segura."
                                            </p>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="background-color:#2a75bb; color:white; text-align:center; padding:20px; border-top: 4px solid #333333;">
                                            <p style="margin:0; font-size:13px; font-weight: bold; letter-spacing: 1px;">
                                                © 2026 Pokémon Service • ¡Atrápalos ya!
                                            </p>
                                            <p style="margin:5px 0 0 0; font-size:11px; color: #e0e0e0;">
                                                Pueblo Paleta, Región de Kanto.
                                            </p>
                                        </td>
                                    </tr>

                                </table>

                            </td>
                        </tr>
                    </table>

                </body>
                </html>
                """
                .formatted(link);

        helper.setText(contenidoHtml, true);

        mailSender.send(message);
    }
}
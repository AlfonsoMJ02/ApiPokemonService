package com.ApiPokemonService.ApiPokemonService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ApiPokemonService.ApiPokemonService.JPA.Peticion;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoVerificacion(String to, String token) throws MessagingException {

        String link = "http://192.167.0.61:8080/auth/verify?token=" + token;

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

    public void enviarCorreoRecuperacion(String to, String token)
            throws MessagingException {

        String link = "http://192.167.0.61:4200/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);

        helper.setSubject("Recuperación de contraseña");

        String contenidoHtml = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                </head>

                <body style="margin:0;
                             padding:0;
                             font-family:'Helvetica Neue',
                             Helvetica, Arial, sans-serif;
                             background-color:#e0e0e0;">

                    <table width="100%%"
                           cellpadding="0"
                           cellspacing="0"
                           style="background-color:#e0e0e0;
                                  padding:40px 0;">

                        <tr>
                            <td align="center">

                                <table width="550"
                                       cellpadding="0"
                                       cellspacing="0"

                                       style="background-color:#ffffff;
                                              border:4px solid #333333;
                                              border-radius:20px;
                                              overflow:hidden;

                                              box-shadow:
                                              0 10px 25px rgba(0,0,0,0.2);">

                                    <tr>
                                        <td style="background-color:#ee1515;
                                                   padding:30px;
                                                   text-align:center;
                                                   border-bottom:
                                                   12px solid #333333;
                                                   position:relative;">

                                            <h1 style="margin:0;
                                                       color:white;
                                                       text-transform:uppercase;
                                                       letter-spacing:2px;

                                                       text-shadow:
                                                       2px 2px #000;">

                                                ¡Recuperación Pokémon!
                                            </h1>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding:40px;
                                                   text-align:center;

                                                   background-image:
                                                   radial-gradient(
                                                   #ffffff 70%%,
                                                   #f9f9f9 100%%);">

                                            <h2 style="color:#2a75bb;
                                                       font-size:24px;
                                                       margin-bottom:10px;">

                                                ¡Hola, Entrenador!
                                            </h2>

                                            <p style="color:#444;
                                                      font-size:16px;
                                                      line-height:1.6;">

                                                Parece que olvidaste tu contraseña
                                                para acceder a la Pokédex oficial.

                                                <br><br>

                                                No te preocupes,
                                                tu aventura Pokémon todavía
                                                puede continuar.

                                                <br><br>

                                                Haz clic en el botón de abajo
                                                para restablecer tu contraseña
                                                y volver al combate.
                                            </p>

                                            <a href="%s"

                                               style="display:inline-block;
                                                      margin-top:25px;
                                                      padding:15px 35px;

                                                      background-color:#ffcb05;
                                                      color:#2a75bb;

                                                      text-decoration:none;

                                                      font-weight:bold;
                                                      font-size:18px;

                                                      border:
                                                      3px solid #2a75bb;

                                                      border-radius:50px;

                                                      box-shadow:
                                                      0 4px 0 #c7a008;">

                                                ¡RECUPERAR CONTRASEÑA!
                                            </a>

                                            <p style="margin-top:35px;
                                                      font-size:14px;
                                                      color:#cc0000;
                                                      font-weight:bold;">

                                                Este enlace expirará en 5 minutos.
                                            </p>

                                            <p style="margin-top:40px;
                                                      font-size:12px;
                                                      color:#888;
                                                      font-style:italic;">

                                                "Si no solicitaste recuperar tu
                                                contraseña, puedes ignorar este
                                                mensaje de forma segura."
                                            </p>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="background-color:#2a75bb;
                                                   color:white;
                                                   text-align:center;
                                                   padding:20px;

                                                   border-top:
                                                   4px solid #333333;">

                                            <p style="margin:0;
                                                      font-size:13px;
                                                      font-weight:bold;
                                                      letter-spacing:1px;">

                                                © 2026 Pokémon Service
                                                • ¡Atrápalos ya!
                                            </p>

                                            <p style="margin:5px 0 0 0;
                                                      font-size:11px;
                                                      color:#e0e0e0;">

                                                Pueblo Paleta,
                                                Región de Kanto.
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

    public void enviarCorreoEstadoPeticion(Peticion peticion) throws MessagingException {

        String to = peticion.getUsuario().getEmail();

        String asunto;
        String tituloHeader;
        String mensajeCuerpo;
        String colorHeader;

        if (peticion.getStatus() == 1) {
            asunto = "¡Tu petición ha sido Aceptada!";
            tituloHeader = "¡PETICIÓN ACEPTADA!";
            colorHeader = "#4CAF50";
            mensajeCuerpo = "¡Grandes noticias, Entrenador! Tu petición sobre <b>"
                    + peticion.getDescripcion()
                    + "</b> ha sido aprobada con éxito por el Comité de la Liga Pokémon.";
        } else {
            asunto = "Actualización sobre tu petición";
            tituloHeader = "PETICIÓN DECLINADA";
            colorHeader = "#ee1515";
            mensajeCuerpo = "Hola, Entrenador. Lamentamos informarte que tu petición sobre <b>"
                    + peticion.getDescripcion()
                    + "</b> ha sido declinada en esta ocasión. ¡No te rindas y sigue entrenando!";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(asunto);

        String contenidoHtml = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>

                <body style="margin:0; padding:0; font-family:'Helvetica Neue', Helvetica, Arial, sans-serif; background-color:#e0e0e0;">

                    <table width="100%" cellpadding="0" cellspacing="0" style="background-color:#e0e0e0; padding:40px 0;">
                        <tr>
                            <td align="center">

                                <table width="550" cellpadding="0" cellspacing="0"
                                       style="background-color:#ffffff;
                                              border:4px solid #333333;
                                              border-radius:20px;
                                              overflow:hidden;
                                              box-shadow:0 10px 25px rgba(0,0,0,0.2);">

                                    <tr>
                                        <td style="background-color:[COLOR_HEADER];
                                                   padding:30px;
                                                   text-align:center;
                                                   border-bottom:12px solid #333333;">

                                            <h1 style="margin:0;
                                                       color:white;
                                                       text-transform:uppercase;
                                                       letter-spacing:2px;
                                                       text-shadow:2px 2px #000;">
                                                [TITULO_HEADER]
                                            </h1>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding:40px;
                                                   text-align:center;
                                                   background-image: radial-gradient(#ffffff 70%, #f9f9f9 100%);">

                                            <h2 style="color:#2a75bb; font-size:24px; margin-bottom:10px;">
                                                ¡Hola, Entrenador!
                                            </h2>

                                            <p style="color:#444; font-size:16px; line-height:1.6;">
                                                [MENSAJE_CUERPO]
                                            </p>

                                            <p style="margin-top:20px; font-size:14px; color:#666;">
                                                <b>ID de la Petición:</b> #[ID_PETICION]
                                            </p>

                                            <p style="margin-top:40px; font-size:12px; color:#888; font-style:italic;">
                                                Si tienes alguna duda o reclamo, puedes presentarte en el Centro Pokémon más cercano.
                                            </p>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="background-color:#2a75bb; color:white; text-align:center; padding:20px; border-top:4px solid #333333;">
                                            <p style="margin:0; font-size:13px; font-weight:bold; letter-spacing:1px;">
                                                © 2026 Pokémon Service • ¡Atrápalos ya!
                                            </p>
                                            <p style="margin:5px 0 0 0; font-size:11px; color:#e0e0e0;">
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
                """;

        contenidoHtml = contenidoHtml
                .replace("[COLOR_HEADER]", colorHeader)
                .replace("[TITULO_HEADER]", tituloHeader)
                .replace("[MENSAJE_CUERPO]", mensajeCuerpo)
                .replace("[ID_PETICION]", String.valueOf(peticion.getIdPeticion()));

        helper.setText(contenidoHtml, true);

        mailSender.send(message);
    }
}

package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.DAO.UsuarioDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.VerificacionToken;
import com.ApiPokemonService.ApiPokemonService.Service.EmailService;
import com.ApiPokemonService.ApiPokemonService.Service.JwtUtil;
import com.ApiPokemonService.ApiPokemonService.Service.VerificacionTokenService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://192.167.0.171:4200")
public class LoginRestController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificacionTokenService verificacionTokenService;

    @Autowired
    private UsuarioDAOImplementation usuarioDAO;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioRequest) {

        Usuario usuario = entityManager
                .createQuery("FROM Usuario WHERE email = :email", Usuario.class)
                .setParameter("email", usuarioRequest.getEmail())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }

        if (!passwordEncoder.matches(usuarioRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }

        if (usuario.getVerified() == 0) {

            String token = verificacionTokenService.generarToken(usuario, "VERIFY");
            try {
                emailService.enviarCorreoVerificacion(usuario.getEmail(), token);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al enviar correo de verificación");
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Debes verificar tu cuenta");
        }

        String jwt = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRol().getNombre());

        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login correcto");
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Sesión cerrada");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verificar(@RequestParam String token) {

        VerificacionToken vToken = verificacionTokenService.buscarPorToken(token);

        if (vToken == null) {
            return ResponseEntity.badRequest().body("Token inválido");
        }

        if (!vToken.getType().equals("VERIFY")) {
            return ResponseEntity.badRequest().body("Tipo de token inválido");
        }

        if (vToken.getUsed() == 1) {
            return ResponseEntity.badRequest().body("Token ya usado");
        }

        if (vToken.getExpirationDate().before(new Date())) {
            return ResponseEntity.badRequest().body("Token expirado");
        }

        Usuario usuario = vToken.getUsuario();
        usuario.setVerified(1);

        usuarioDAO.Update(usuario);

        vToken.setUsed(1);
        verificacionTokenService.actualizarToken(vToken);

        String jwt = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().getNombre());

        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("""
                            <html>
                                <head>
                                    <title>Cuenta verificada</title>
                                </head>
                                <body style="font-Family:sans-serif;text-align:center;margin-top:50px;">
                                    <h2>Cuenta verificada con exito</h2>
                                    <p>Puedes regresar a la pagina anterior</p>
                                </body>
                            </html>
                        """);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {

        if (request.getCookies() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = null;

        for (var cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                
                Usuario usuario = entityManager.createQuery(
                        "FROM Usuario WHERE email = :email", Usuario.class)
                        .setParameter("email", email).getSingleResult();
                
                usuario.setPassword(null);
                
                return ResponseEntity.ok(usuario);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody Usuario usuarioRequest) throws MessagingException {

        Usuario usuario = entityManager
                .createQuery(
                        "FROM Usuario WHERE email = :email",
                        Usuario.class
                )
                .setParameter("email", usuarioRequest.getEmail())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (usuario != null) {

            String token = verificacionTokenService
                    .generarToken(usuario, "RESET_PASSWORD");

            emailService.enviarCorreoRecuperacion(
                    usuario.getEmail(),
                    token
            );
        }

        return ResponseEntity.ok(
                "Si el correo existe, se enviaron instrucciones"
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody Usuario usuarioRequest) {

        VerificacionToken vToken
                = verificacionTokenService.buscarPorToken(token);

        if (vToken == null) {
            return ResponseEntity.badRequest()
                    .body("Token inválido");
        }

        if (!vToken.getType().equals("RESET_PASSWORD")) {
            return ResponseEntity.badRequest()
                    .body("Tipo de token inválido");
        }

        if (vToken.getUsed() == 1) {
            return ResponseEntity.badRequest()
                    .body("Token ya usado");
        }

        if (vToken.getExpirationDate().before(new Date())) {
            return ResponseEntity.badRequest()
                    .body("Token expirado");
        }

        Usuario usuario = vToken.getUsuario();

        usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword())
        );

        usuarioDAO.UpdatePassword(usuario);

        vToken.setUsed(1);

        verificacionTokenService.actualizarToken(vToken);

        return ResponseEntity.ok(
                "Contraseña actualizada correctamente"
        );
    }
}

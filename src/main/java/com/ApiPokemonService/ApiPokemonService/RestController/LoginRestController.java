package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.DAO.UsuarioDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.VerificacionToken;
import com.ApiPokemonService.ApiPokemonService.Service.EmailService;
import com.ApiPokemonService.ApiPokemonService.Service.JwtUtil;
import com.ApiPokemonService.ApiPokemonService.Service.VerificacionTokenService;
import jakarta.persistence.EntityManager;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
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
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(usuarioRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        if (usuario.getVerified() == 0) {

            String token = verificacionTokenService.generarToken(usuario);

            emailService.enviarCorreoVerificacion(usuario.getEmail(), token);

            return ResponseEntity.ok("Debes verificar tu cuenta");
        }

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRol().getNombre()
        );

        return ResponseEntity.ok(token);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verificar(@RequestParam String token) {

        VerificacionToken vToken = verificacionTokenService.buscarPorToken(token);

        if (vToken == null) {
            return ResponseEntity.badRequest().body("Token inválido");
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
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.LOCATION, "http://localhost:4200/home")
                .build();
    }
}

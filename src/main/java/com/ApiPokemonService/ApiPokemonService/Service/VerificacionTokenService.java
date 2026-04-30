package com.ApiPokemonService.ApiPokemonService.Service;

import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.VerificacionToken;
import com.ApiPokemonService.ApiPokemonService.Repository.VerificacionTokenRepository;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificacionTokenService {

    @Autowired
    private VerificacionTokenRepository tokenRepository;

    @Transactional
    public String generarToken(Usuario usuario) {

        tokenRepository.deleteByUsuario(usuario);

        String token = UUID.randomUUID().toString();

        VerificacionToken v = new VerificacionToken();
        v.setToken(token);
        v.setUsuario(usuario);
        v.setUsed(0);
        v.setExpirationDate(new Date(System.currentTimeMillis() + (15 * 60 * 1000)));

        tokenRepository.save(v);

        return token;
    }

    public VerificacionToken buscarPorToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void actualizarToken(VerificacionToken token) {
        tokenRepository.save(token);
    }
}

package com.ApiPokemonService.ApiPokemonService.Repository;

import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.VerificacionToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificacionTokenRepository extends JpaRepository<VerificacionToken, Long> {
    VerificacionToken findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}

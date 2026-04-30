package com.ApiPokemonService.ApiPokemonService.DAO;

import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;

import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;

import jakarta.persistence.EntityManager;

public class PokemonDAOImplementation implements IPokemon {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result<?> delete(Pokemon pokemon) {

        Result<?> result = new Result<>();
        try {
            Pokemon pokemonJPA = entityManager.find(pokemon.getClass(), pokemon.getIdPokemon());
            if (pokemonJPA != null) {
                entityManager.remove(pokemonJPA);
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "pokemon no encontrado";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}

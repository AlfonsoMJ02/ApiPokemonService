package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;

public interface IPokemon {
    Result<?> delete(Pokemon pokemon);
}

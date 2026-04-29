package com.ApiPokemonService.ApiPokemonService.DAO;


import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;

public interface IPokemonFavorito {

    Result getByUserPokemon(int idUsuario, int idPokemon);

     Result GetAllPokemonFavorites(int idUsuario);
    Result DeletePokemonFavorite(int idUsuario, Pokemon pokemon);
    Result AddPokemonFavorite(int idUsuario, Pokemon pokemonRecibido);

}

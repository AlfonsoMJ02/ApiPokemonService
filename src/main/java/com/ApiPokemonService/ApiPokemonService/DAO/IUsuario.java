package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;

public interface IUsuario {
    Result GetAll();
    Result GetById(int idUsuario);
    Result Add(Usuario usuario);
    Result Update(Usuario usuario);
    Result eliminar(int idUsuario);

}

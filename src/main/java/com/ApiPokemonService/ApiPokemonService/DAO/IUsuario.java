package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;

public interface IUsuario {
    Result GetAll();
    Result Add(Usuario usuario);
}

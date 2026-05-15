package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Peticion;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;


public interface IPeticion {
    Result getAll();
    Result add(Peticion peticion);
    Result accept(int idUsuario);
    Result decline(int idUsuario);
    
    
}

package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Peticion;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;


public interface IPeticion {
    Result getAll();
    Result getAllById(int idUsuario);
    Result add(Peticion peticion);
    Result accept(Peticion peticion);
    Result decline(Peticion peticion);
    
    
}

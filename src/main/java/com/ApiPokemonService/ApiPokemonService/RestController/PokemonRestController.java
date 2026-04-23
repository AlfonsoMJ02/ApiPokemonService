package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.JPA.DTO.PokemonDTO;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.Service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonRestController {
        
    @Autowired
    private PokemonService pokemonService;

    @GetMapping("getAll")
    public Result<PokemonDTO> getAllPokemones(){
        return pokemonService.getAllPokemones(); 
    }
}

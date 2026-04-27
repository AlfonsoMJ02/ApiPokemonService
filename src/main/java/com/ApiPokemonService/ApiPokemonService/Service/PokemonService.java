package com.ApiPokemonService.ApiPokemonService.Service;

import com.ApiPokemonService.ApiPokemonService.DTO.PokemonDTO;
import com.ApiPokemonService.ApiPokemonService.DTO.PokemonResponse;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final String URL = "https://pokeapi.co/api/v2/";
    
    public Result<PokemonDTO> getAllPokemones(){
        Result<PokemonDTO> result = new Result<>();
        
        try {
            PokemonResponse pokemonResponse = restTemplate.getForObject(URL + "pokemon", PokemonResponse.class);
            
            result.objects = pokemonResponse.getResults();
            result.correct = true;
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }
}

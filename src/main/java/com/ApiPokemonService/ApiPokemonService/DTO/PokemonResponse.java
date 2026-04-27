package com.ApiPokemonService.ApiPokemonService.DTO;

import java.util.List;

public class PokemonResponse {
    private List<PokemonDTO> results;

    public List<PokemonDTO> getResults() {
        return results;
    }

    public void setResults(List<PokemonDTO> results) {
        this.results = results;
    }
}

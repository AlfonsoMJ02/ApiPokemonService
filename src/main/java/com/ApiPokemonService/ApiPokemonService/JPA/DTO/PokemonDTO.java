package com.ApiPokemonService.ApiPokemonService.JPA.DTO;

public class PokemonDTO {
    private String name;
    private String url;
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

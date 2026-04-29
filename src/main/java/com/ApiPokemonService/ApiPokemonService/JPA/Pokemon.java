package com.ApiPokemonService.ApiPokemonService.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Pokemon {
    @Id
    @Column(name = "idpokemon")
    private int idPokemon;
    
    @Column(name = "name")
    private String name;

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    

}

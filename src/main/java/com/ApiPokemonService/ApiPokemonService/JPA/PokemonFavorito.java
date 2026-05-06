package com.ApiPokemonService.ApiPokemonService.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pokemonfavorito")
public class PokemonFavorito {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "idpokemonfavorito")
   private int idPokemonFavorito;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "idusuario")
   @JsonIgnore
   private Usuario usuario;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "idpokemon")
   
   private Pokemon pokemon;

   public Usuario getUsuario() {
       return usuario;
   }

   public void setUsuario(Usuario usuario) {
       this.usuario = usuario;
   }

   public Pokemon getPokemon() {
       return pokemon;
   }

   public void setPokemon(Pokemon pokemon) {
       this.pokemon = pokemon;
   }
}

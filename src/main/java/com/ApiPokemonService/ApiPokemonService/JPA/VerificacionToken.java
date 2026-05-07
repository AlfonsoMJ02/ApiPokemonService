package com.ApiPokemonService.ApiPokemonService.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "VERIFICATIONTOKEN")
public class VerificacionToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtoken")
    private Long idToken;

    @Column(name = "token")
    private String token;

    @Column(name = "expirationdate")
    private Date expirationDate;

    @Column(name = "used")
    private int used;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private Usuario usuario;

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
    
    public String getType(){
        return type;
    }
    
    public void setType(String type){
        this.type = type;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

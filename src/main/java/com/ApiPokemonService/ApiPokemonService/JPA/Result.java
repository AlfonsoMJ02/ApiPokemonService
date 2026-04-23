package com.ApiPokemonService.ApiPokemonService.JPA;

import java.util.List;

public class Result<T> {
    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public Object object;
    public List<T> objects;
}
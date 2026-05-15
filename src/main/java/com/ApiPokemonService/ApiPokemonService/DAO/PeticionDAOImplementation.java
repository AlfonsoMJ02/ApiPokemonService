/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Peticion;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ALIEN62
 */
@Repository

public class PeticionDAOImplementation implements IPeticion {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result getAll() {
        Result result = new Result();
        try {
            String jpql = "SELECT p.idPeticion, p.fechaHora, p.status, u.idUsuario, u.userName "
                    + "FROM Peticion p JOIN p.usuario u";

            List<Object[]> filas = entityManager.createQuery(jpql).getResultList();
            List<Peticion> peticionesCorta = new ArrayList<>();

            for (Object[] fila : filas) {
                Peticion p = new Peticion();
                p.setIdPeticion((int) fila[0]);
                p.setFechaHora((LocalDateTime) fila[1]);
                p.setStatus((int) fila[2]);
                Usuario u = new Usuario();
                u.setIdUsuario((int) fila[3]);
                u.setUserName((String) fila[4]);

                p.setUsuario(u);
                peticionesCorta.add(p);
            }

            result.objects = new ArrayList<>(peticionesCorta);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result accept(int idUsuario) {
        Result result = new Result();
        try {

        } catch (Exception ex) {

        }
        return result;
    }

    @Override
    public Result decline(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    @Transactional
    public Result add(Peticion peticion) {
        Result result = new Result();
        try {
            if (peticion.getUsuario() == null || peticion.getUsuario().getIdUsuario() == 0) {
                result.correct = false;
                result.errorMessage = "El usuario es obligatorio para crear la petición.";
                return result;
            }

            String jpql = "SELECT p FROM Peticion p WHERE p.usuario.idUsuario = :idUsuario";
            List<Peticion> peticionesExistentes = entityManager.createQuery(jpql, Peticion.class)
                    .setParameter("idUsuario", peticion.getUsuario().getIdUsuario())
                    .getResultList();

            if (!peticionesExistentes.isEmpty()) {
                result.correct = false;
                result.errorMessage = "Ya hay una petición activa para este usuario (ID: "
                        + peticion.getUsuario().getIdUsuario() + ")";
            } else {
                if (peticion.getFechaHora() == null) {
                    peticion.setFechaHora(java.time.LocalDateTime.now());
                }

                entityManager.persist(peticion);
                result.correct = true;
                result.object = peticion;
            }

        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }

}

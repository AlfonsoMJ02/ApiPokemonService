package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImplementation implements IUsuario {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();

        try {
            TypedQuery<Usuario> query = entityManager.createQuery("From Usuario", Usuario.class);
            List<Usuario> usuarios = query.getResultList();
            result.objects = new ArrayList<>(usuarios);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    @Transactional
    public Result Add(Usuario usuario) {
        Result result = new Result();

        try {
            TypedQuery<Long> query = entityManager
                    .createQuery("SELECT COUNT(u) FROM Usuario "
                            + "u WHERE u.email = :email", Long.class);
            query.setParameter("email", usuario.getEmail());
            Long count = query.getSingleResult();

            if (count > 0) {
                result.correct = false;
                result.errorMessage = "Este correo ya esta registrado";
                return result;
            }

            Rol rolBD = entityManager.find(Rol.class, usuario.getRol().getIdRol());
            usuario.setRol(rolBD);
            
            entityManager.persist(usuario);

            result.object = usuario;
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }
}

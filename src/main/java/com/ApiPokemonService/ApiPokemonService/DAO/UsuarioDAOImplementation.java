package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;
import com.ApiPokemonService.ApiPokemonService.JPA.PokemonFavorito;

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

    @Autowired
    private PokemonFavoritoDAOImplementation pokemonFavoritoDAOImplementation;

    @Override
    public Result<Usuario> GetAll() {
        Result<Usuario> result = new Result<Usuario>();

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
    public Result GetById(int idUsuario) {
        Result<Usuario> result = new Result<Usuario>();

        try {
            Usuario usuario = entityManager.find(Usuario.class, idUsuario);
            if (usuario != null) {
                result.object = usuario;
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
            }
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

    @Transactional
    @Override
    public Result<?> Delete(int idUsuario) {
        Result<?> result = new Result<>();

        try {
            Usuario usuario = entityManager.find(Usuario.class, idUsuario);

            if (usuario == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
                return result;
            }
            List<PokemonFavorito> favoritos = (List<PokemonFavorito>) pokemonFavoritoDAOImplementation
                    .GetAllPokemonFavorites(idUsuario).object;

            for (PokemonFavorito favorito : favoritos) {

                Result resultDeleteFavorite = pokemonFavoritoDAOImplementation.DeletePokemonFavorite(idUsuario,
                        favorito.getPokemon());
                if (!resultDeleteFavorite.correct) {
                    result.correct = false;
                    result.errorMessage = "Error al eliminar pokemon favorito: " + resultDeleteFavorite.errorMessage;
                    return result;
                }

            }

            entityManager.remove(usuario);
            entityManager.flush();

            result.correct = true;
            result.errorMessage = "Usuario y relaciones eliminadas correctamente";

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result<Usuario> Update(Usuario usuario) {
        Result<Usuario> result = new Result<Usuario>();

        try {
            TypedQuery<Long> query = entityManager
                    .createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.email = :email AND u.idUsuario <> :idUsuario",
                            Long.class);

            query.setParameter("email", usuario.getEmail());
            query.setParameter("idUsuario", usuario.getIdUsuario());

            Long count = query.getSingleResult();

            if (count > 0) {
                result.correct = false;
                result.errorMessage = "Este correo ya esta registrado";
                return result;
            }
            Usuario usuarioBD = entityManager.find(Usuario.class, usuario.getIdUsuario());
            if (usuarioBD == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
                return result;
            } else {
                usuarioBD.setNombre(usuario.getNombre());
                usuarioBD.setUserName(usuario.getUserName());
                usuarioBD.setApellidoMaterno(usuario.getApellidoMaterno());
                usuarioBD.setApellidoPaterno(usuario.getApellidoPaterno());
                usuarioBD.setTelefono(usuario.getTelefono());
                usuarioBD.setCelular(usuario.getCelular());
                usuarioBD.setEmail(usuario.getEmail());
                usuarioBD.setRol(usuario.getRol());
            }

            Rol rolBD = entityManager.find(Rol.class, usuario.getRol().getIdRol());
            usuario.setRol(rolBD);

            entityManager.merge(usuarioBD);

            result.object = usuarioBD;
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result GetByEmail(String email) {
        return null;
    }

}

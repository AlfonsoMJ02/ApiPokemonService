package com.ApiPokemonService.ApiPokemonService.DAO;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ApiPokemonService.ApiPokemonService.JPA.PokemonFavorito;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class PokemonFavoritoDAOImplementation implements IPokemonFavorito {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result<?> getByUserPokemon(int idUsuario, int idPokemon) {
        Result<PokemonFavorito> result = new Result<PokemonFavorito>();
        try {
            String jpql = "SELECT p FROM PokemonFavorito p JOIN FETCH p.pokemon JOIN FETCH p.usuario WHERE p.usuario.idUsuario = :idUsuario AND p.pokemon.idPokemon = :idPokemon";

            List<PokemonFavorito> favorito = entityManager.createQuery(jpql, PokemonFavorito.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idPokemon", idPokemon)

                    .getResultList();
            result.object = favorito.get(0);
            if (favorito.size() > 0) {
                result.correct = true;
                result.errorMessage = "Elementos recuperados";

            } else {
                result.correct = false;
                result.errorMessage = "Sin elementos";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetAllPokemonFavorites(int idUsuario) {
        Result<Object> result = new Result();
        try {
            String jpql = "SELECT p FROM PokemonFavorito p JOIN FETCH p.pokemon JOIN FETCH p.usuario WHERE p.usuario.idUsuario = :idParam";

            List<PokemonFavorito> favoritos = entityManager.createQuery(jpql, PokemonFavorito.class)
                    .setParameter("idParam", idUsuario)
                    .getResultList();

            result.object = favoritos;
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
    public Result<?> DeletePokemonFavorite(int idUsuario, Pokemon pokemonRecibido) {
        Result<PokemonFavorito> result = new Result<PokemonFavorito>();
        try {
            int eliminados = entityManager.createQuery(
                    "DELETE FROM PokemonFavorito p WHERE p.usuario.idUsuario = :idUsuario AND p.pokemon.idPokemon = :idPokemon")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idPokemon", pokemonRecibido.getIdPokemon())
                    .executeUpdate();

            if (eliminados > 0) {
                result.correct = true;

                long conteo = (long) entityManager.createQuery(
                        "SELECT COUNT(p) FROM PokemonFavorito p WHERE p.pokemon.idPokemon = :idPokemon")
                        .setParameter("idPokemon", pokemonRecibido.getIdPokemon())
                        .getSingleResult();

                if (conteo == 0) {
                    entityManager.createQuery(
                            "DELETE FROM Pokemon p WHERE p.idPokemon = :idPokemon")
                            .setParameter("idPokemon", pokemonRecibido.getIdPokemon())
                            .executeUpdate();
                }

            } else {
                result.correct = false;
                result.errorMessage = "Pokemon favorito no encontrado";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result AddPokemonFavorite(int idUsuario,
            Pokemon pokemonRecibido) {

        Result<PokemonFavorito> result = new Result();
        try {
            Usuario usuario = entityManager.find(Usuario.class, idUsuario);
            Pokemon pokemon = entityManager.find(Pokemon.class, pokemonRecibido.getIdPokemon());

            PokemonFavorito pokemonFavorito;
            if (usuario != null) {
                if (pokemon == null) {
                    entityManager.persist(pokemonRecibido);
                }
                pokemonFavorito = new PokemonFavorito();
                pokemonFavorito.setPokemon(pokemonRecibido);

                pokemonFavorito.setUsuario(usuario);

                if (getByUserPokemon(idUsuario,
                        pokemonRecibido.getIdPokemon()).correct) {
                    result.errorMessage = "Pokemon favorito ya almacenado para el usuario: " + usuario.getNombre();
                } else {
                    result.errorMessage = "Pokemon Favorito nuevo almacenado para el usuario: " + usuario.getNombre();
                    entityManager.persist(pokemonFavorito);
                }

                result.correct = true;
                result.object = pokemonFavorito;

            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result<Pokemon> GetMostFavoritePokemon() {
        Result<Pokemon> result = new Result<>();
        try {
            Object topPokemon = entityManager.createQuery(
                    "SELECT p.pokemon.name, COUNT(p) as total " +
                            "FROM PokemonFavorito p " +
                            "GROUP BY p.pokemon.idPokemon, p.pokemon.name " +
                            "ORDER BY total DESC")
                    .setMaxResults(1)
                    .getSingleResult();

            result.object = topPokemon;
            result.correct = true;
            result.errorMessage = "Pokémon más seleccionado recuperado";

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result<Object[]> GetFavoritePokemonWithUsers() {
        Result<Object[]> result = new Result<>();
        try {
            List<Object[]> lista = entityManager.createQuery(
                    "SELECT p.pokemon.idPokemon, p.pokemon.name, u.userName " +
                            "FROM PokemonFavorito p " +
                            "JOIN p.usuario u " +
                            "ORDER BY p.pokemon.idPokemon",
                    Object[].class)
                    .getResultList();

            result.objects = lista;
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}

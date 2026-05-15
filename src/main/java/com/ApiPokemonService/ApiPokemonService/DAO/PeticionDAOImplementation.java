
package com.ApiPokemonService.ApiPokemonService.DAO;

import com.ApiPokemonService.ApiPokemonService.JPA.Peticion;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository

public class PeticionDAOImplementation implements IPeticion {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result<Peticion> getAll() {
        Result<Peticion> result = new Result<Peticion>();
        try {
            String jpql = "SELECT p.idPeticion, p.fechaHora, p.status, u.idUsuario, u.userName, u.email, p.descripcion "
                    + "FROM Peticion p JOIN p.usuario u";

            List<Object[]> filas = entityManager.createQuery(jpql).getResultList();
            List<Peticion> peticionesCorta = new ArrayList<>();

            for (Object[] fila : filas) {
                Peticion p = new Peticion();
                p.setIdPeticion((int) fila[0]);
                p.setFechaHora((LocalDateTime) fila[1]);
                p.setStatus((int) fila[2]);
                p.setDescripcion((String) fila[6]);
                Usuario u = new Usuario();
                u.setIdUsuario((int) fila[3]);
                u.setUserName((String) fila[4]);
                u.setEmail((String) fila[5]);

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
    @Transactional
    public Result<Peticion> add(Peticion peticion) {
        Result<Peticion> result = new Result<Peticion>();
        try {
            if (peticion.getUsuario() == null || peticion.getUsuario().getIdUsuario() == 0) {
                result.correct = false;
                result.errorMessage = "El usuario es obligatorio para crear la petición.";
                return result;
            }

            Usuario usuario = entityManager.find(Usuario.class, peticion.getUsuario().getIdUsuario());

            if (usuario == null) {
                result.correct = false;
                result.errorMessage = "Recurso no encontrado.";
                return result;
            } else {
                if (usuario.getRol().getIdRol() == 1) {
                    result.correct = false;
                    result.errorMessage = "Ya es un Maestro";
                    return result;
                }
            }

            String jpql = "SELECT p FROM Peticion p WHERE p.usuario.idUsuario = :idUsuario";
            List<Peticion> peticionesExistentes = entityManager.createQuery(jpql, Peticion.class)
                    .setParameter("idUsuario", peticion.getUsuario().getIdUsuario())
                    .getResultList();

            boolean puedeProceder = true;

            if (!peticionesExistentes.isEmpty()) {
                for (Peticion peticion2 : peticionesExistentes) {

                    int statusExistente = peticion2.getStatus();
                    if (statusExistente == 0) {
                        result.correct = false;
                        result.errorMessage = "Ya hay una petición activa para este usuario (ID: "
                                + peticion.getUsuario().getIdUsuario() + ")";
                        puedeProceder = false;
                    }
                    if (statusExistente == 1) {
                        result.correct = false;
                        result.errorMessage = "La petición ya fue aceptada para el usuario (ID: "
                                + peticion.getUsuario().getIdUsuario() + ")";
                        puedeProceder = false;
                    }

                }

            }

            if (puedeProceder) {
                if (peticion.getFechaHora() == null) {
                    peticion.setFechaHora(java.time.LocalDateTime.now());
                }
                peticion.getUsuario().setUserName(usuario.getUserName());
                peticion.getUsuario().setEmail(usuario.getEmail());

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

    @Transactional
    @Override
    public Result<Peticion> accept(Peticion peticion) {
        Result<Peticion> result = new Result<Peticion>();
        try {
            if (peticion == null || peticion.getIdPeticion() == 0) {
                result.correct = false;
                result.errorMessage = "ID de petición no válido.";
                return result;
            }

            Peticion peticionExistente = entityManager.find(Peticion.class, peticion.getIdPeticion());

            if (peticionExistente != null) {
                if (peticionExistente.getStatus() == 0) {
                    Usuario usuario = entityManager.find(Usuario.class, peticionExistente.getUsuario().getIdUsuario());

                    if (usuario != null) {
                        Rol nuevoRol = entityManager.getReference(Rol.class, 1);
                        usuario.setRol(nuevoRol);
                        entityManager.merge(usuario);
                    }

                    peticionExistente.setFechaHora(LocalDateTime.now());
                    peticionExistente.setStatus(1);
                    peticionExistente.setDescripcion(peticion.getDescripcion());
                    entityManager.merge(peticionExistente);

                    result.correct = true;
                    result.object = peticionExistente;

                } else if (peticionExistente.getStatus() == 1 || peticionExistente.getStatus() == 2) {
                    result.correct = false;
                    result.errorMessage = "La solicitud ya fue atendida previamente.";
                }
            } else {
                result.correct = false;
                result.errorMessage = "La petición no existe.";
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
    public Result<Peticion> decline(Peticion peticion) {
        Result<Peticion> result = new Result<Peticion>();
        try {
            if (peticion == null || peticion.getIdPeticion() == 0) {
                result.correct = false;
                result.errorMessage = "ID de petición no válido.";
                return result;
            }

            Peticion peticionExistente = entityManager.find(Peticion.class, peticion.getIdPeticion());

            if (peticionExistente != null) {
                if (peticionExistente.getStatus() == 0) {
                    Usuario usuario = entityManager.find(Usuario.class, peticionExistente.getUsuario().getIdUsuario());

                    if (usuario != null) {
                        Rol rolDeclinado = entityManager.getReference(Rol.class, 2);

                        usuario.setRol(rolDeclinado);
                        entityManager.merge(usuario);
                    }

                    peticionExistente.setFechaHora(LocalDateTime.now());
                    peticionExistente.setStatus(2);
                    peticionExistente.setDescripcion(peticion.getDescripcion());
                    entityManager.merge(peticionExistente);

                    result.correct = true;
                    result.object = peticionExistente;

                } else if (peticionExistente.getStatus() == 2 || peticionExistente.getStatus() == 1) {
                    result.correct = false;
                    result.errorMessage = "La solicitud ya fue atendida previamente.";
                }
            } else {
                result.correct = false;
                result.errorMessage = "La petición no existe.";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result<Peticion> getAllById(int idUsuario) {
        Result<Peticion> result = new Result<Peticion>();
        try {
            Usuario usuario = entityManager.find(Usuario.class, idUsuario);
            if (usuario == null) {
                result.correct = false;
                result.errorMessage = "El usuario no existe ";
            } else {
                String jpql = "SELECT p FROM Peticion p WHERE p.usuario.idUsuario = :idUsuario";
                List<Peticion> peticionesExistentes = entityManager.createQuery(jpql, Peticion.class)
                        .setParameter("idUsuario", idUsuario)
                        .getResultList();
                result.objects= new ArrayList<>(peticionesExistentes);
                result.correct = true;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}

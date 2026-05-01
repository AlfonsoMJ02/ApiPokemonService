package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.DAO.PokemonFavoritoDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.DAO.RolDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.DAO.UsuarioDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.JPA.Pokemon;
import com.ApiPokemonService.ApiPokemonService.JPA.PokemonFavorito;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioRestController {

    @Autowired
    private RolDAOImplementation rolDAO;

    @Autowired
    private UsuarioDAOImplementation usuarioDAO;

    @Autowired
    private PokemonFavoritoDAOImplementation pokemonFavoritoDAOImplementation;

    @Autowired
    CustomUserDetailsService usuarioService;

    @GetMapping()
    public ResponseEntity<Result<Usuario>> getAll() {
        Result<Usuario> result = new Result<Usuario>();
        try {
            result = usuarioDAO.GetAll();
            if (result.correct) {
                if (result.objects.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(result);

            }else{
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().body(result);
        }


    }

    @PostMapping("/add")
    public ResponseEntity<Result<Usuario>> add(@RequestBody Usuario usuario) {
        Result result = usuarioService.registro(usuario);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }

    @GetMapping("/rol")
    public ResponseEntity<Result<Rol>> getAllRol() {
        Result<Rol> result = new Result<Rol>();
        try {
            result= rolDAO.GetAll();
            if (result.correct) {
                return ResponseEntity.ok(result);

            }else{
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().body(result);
        }
        
    }

    // POKEMON FAVORITOS DEL USUARIO ACTUAL
    @GetMapping("/pokeFavs/{idUsuario}")
    public ResponseEntity GetAllPokemonFavorites(@PathVariable("idUsuario") int idUsuario) {
        Result result = new Result<>();
        try {
            result = pokemonFavoritoDAOImplementation.GetAllPokemonFavorites(idUsuario);
            if (result.correct) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PostMapping("/pokeFavs/{idUsuario}")
    public ResponseEntity AddPokemonFavorite(@PathVariable("idUsuario") int idUsuario,
            @RequestBody Pokemon pokemon) {
        Result result = new Result<>();
        try {
            result = pokemonFavoritoDAOImplementation.AddPokemonFavorite(idUsuario, pokemon);
            if (result.correct) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @DeleteMapping("/pokeFavs/{idUsuario}")
    public ResponseEntity DeletePokemonFavorite(@PathVariable("idUsuario") int idUsuario,
            @RequestBody Pokemon pokemon) {
        Result<?> result = new Result<>();
        try {
            result = pokemonFavoritoDAOImplementation.DeletePokemonFavorite(idUsuario, pokemon);
            if (result.correct) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
            return ResponseEntity.internalServerError().body(result);
        }
    }

}

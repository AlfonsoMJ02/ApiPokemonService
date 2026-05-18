package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.DAO.PeticionDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.JPA.Peticion;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/peticion")
public class PeticionRestController {

    @Autowired
    private PeticionDAOImplementation peticionDAOImplementation;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity getAll() {
        Result result = new Result();
        try {
            result = peticionDAOImplementation.getAll();
            if (result.correct) {
                if (result.objects.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity getAllById(@PathVariable("idUsuario") int idUsuario) {
        Result result = new Result();
        try {
            result = peticionDAOImplementation.getAllById(idUsuario);
            if (result.correct) {
                if (result.objects.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping
    public ResponseEntity add(@RequestBody Peticion peticion) {

        Result result = new Result();
        try {
            result = peticionDAOImplementation.add(peticion);
            if (result.correct) {
                if (result.object == null) {
                    return ResponseEntity.noContent().build();
                } else {

                    return ResponseEntity.ok(result);
                }
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().body(result);
        }

    }

    @PutMapping("/accept")
    public ResponseEntity accept(@RequestBody Peticion peticion) {

        Result result = new Result();
        try {
            result = peticionDAOImplementation.accept(peticion);
            if (result.correct) {
                if (result.object == null) {
                    return ResponseEntity.noContent().build();
                } else {
                    emailService.enviarCorreoEstadoPeticion(peticion);
                    return ResponseEntity.ok(result);
                }
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.   getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().body(result);
        }

    }

    @PutMapping("/decline")
    public ResponseEntity decline(@RequestBody Peticion peticion) {

        Result result = new Result();
        try {
            result = peticionDAOImplementation.decline(peticion);
            if (result.correct) {
                if (result.object == null) {
                    return ResponseEntity.noContent().build();  
                } else {

                    emailService.enviarCorreoEstadoPeticion(peticion);
                    return ResponseEntity.ok(result);
                }
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            return ResponseEntity.internalServerError().body(result);
        }

    }

}

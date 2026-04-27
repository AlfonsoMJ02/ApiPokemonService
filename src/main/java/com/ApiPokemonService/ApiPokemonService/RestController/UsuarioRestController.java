package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.DAO.RolDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.DAO.UsuarioDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class UsuarioRestController {
    
    @Autowired
    private RolDAOImplementation rolDAO;
    
    @Autowired
    private UsuarioDAOImplementation usuarioDAO;
    
    @Autowired CustomUserDetailsService usuarioService;
    
    @GetMapping()
    public Result<Usuario> getAll(){
        return usuarioDAO.GetAll();
    }
    
    @PostMapping("/add")
    public ResponseEntity<Result<Usuario>> add(@RequestBody Usuario usuario){
        Result result = usuarioService.registro(usuario);
        
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }
    
    @GetMapping("/rol")
    public Result<Rol> getAllRol(){
        return rolDAO.GetAll();
    }
}

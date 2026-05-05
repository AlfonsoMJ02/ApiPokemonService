package com.ApiPokemonService.ApiPokemonService.RestController;

import com.ApiPokemonService.ApiPokemonService.DAO.RolDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.DAO.UsuarioDAOImplementation;
import com.ApiPokemonService.ApiPokemonService.JPA.Result;
import com.ApiPokemonService.ApiPokemonService.JPA.Rol;
import com.ApiPokemonService.ApiPokemonService.JPA.Usuario;
import com.ApiPokemonService.ApiPokemonService.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMethodSecurity
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
    
    @PutMapping("/update/{idUsuario}")
    public ResponseEntity<?> update(@PathVariable int idUsuario, @RequestBody Usuario usuario){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        String emailLogueado = auth.getName();
        
        boolean esMaestro = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(("ROLE_Maestro")));
        
        Result resultUsuario = usuarioDAO.GetByEmail(emailLogueado);
        
        if (!resultUsuario.correct) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        
        Usuario usuarioLogueado = (Usuario) resultUsuario.object;
        
        if (!esMaestro && usuarioLogueado.getIdUsuario() != idUsuario) {
            return ResponseEntity.status(403).body("No tienes permisos para editar a este usuario");
        }
        
        usuario.setIdUsuario(idUsuario);
        Result result = usuarioDAO.Update(usuario);
        
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
    
    
    @PreAuthorize("hasRole('Maestro')")
    @DeleteMapping("delete/{idUsuario}")
    public ResponseEntity<?> delete(@PathVariable int idUsuario){
        Result result = usuarioDAO.Delete(idUsuario);
        
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(403).body(result);
        }
    }
}

package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.UsuarioDTO;
import com.bugabuga.e_commerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AutenticacionController {

    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(AutenticacionController.class);

    @Autowired
    public AutenticacionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UsuarioDTO> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Inicio registrarUsuario: {}", inicio);
        
        UsuarioDTO nuevoUsuario = usuarioService.registrarUsuario(usuarioDTO);
        
        long fin = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Fin registrarUsuario: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> iniciarSesion(@RequestBody Map<String, String> credenciales) {
        long inicio = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Inicio iniciarSesion: {}", inicio);
        
        String email = credenciales.get("email");
        String password = credenciales.get("password");
        
        UsuarioDTO usuario = usuarioService.autenticarUsuario(email, password);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("usuario", usuario);
        respuesta.put("mensaje", "Inicio de sesión exitoso");
        // Aquí se podría agregar un token JWT si se implementa autenticación basada en tokens
        
        long fin = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Fin iniciarSesion: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> obtenerPerfilUsuario(@RequestParam Long usuarioId) {
        long inicio = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Inicio obtenerPerfilUsuario: {}", inicio);
        
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        
        long fin = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Fin obtenerPerfilUsuario: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/perfil/{id}")
    public ResponseEntity<UsuarioDTO> actualizarPerfilUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Inicio actualizarPerfilUsuario: {}", inicio);
        
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        
        long fin = System.currentTimeMillis();
        logger.info("[AUTENTICACION] Fin actualizarPerfilUsuario: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(usuarioActualizado);
    }
}

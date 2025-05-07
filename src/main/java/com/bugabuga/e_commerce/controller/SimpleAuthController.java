package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.UsuarioDTO;
import com.bugabuga.e_commerce.model.entity.Rol;
import com.bugabuga.e_commerce.model.entity.Usuario;
import com.bugabuga.e_commerce.repository.RolRepository;
import com.bugabuga.e_commerce.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador simplificado para autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/api/auth-simple")
public class SimpleAuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private static final Logger logger = LoggerFactory.getLogger(SimpleAuthController.class);

    @Autowired
    public SimpleAuthController(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Verificar que el email no esté en uso
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                return ResponseEntity.badRequest().body("El email ya está en uso");
            }

            // Crear un nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellido(usuarioDTO.getApellido());
            usuario.setPassword(usuarioDTO.getPassword()); // En un caso real, se debería encriptar la contraseña
            usuario.setActivo(true);

            // Asignar roles
            Set<Rol> roles = new HashSet<>();
            if (usuarioDTO.getRoles() != null && !usuarioDTO.getRoles().isEmpty()) {
                for (String rolNombre : usuarioDTO.getRoles()) {
                    rolRepository.findByNombre(rolNombre)
                            .ifPresent(roles::add);
                }
            }

            // Si no se asignaron roles, asignar rol CLIENTE por defecto
            if (roles.isEmpty()) {
                rolRepository.findByNombre("CLIENTE")
                        .ifPresent(roles::add);
            }

            usuario.setRoles(roles);

            // Guardar el usuario
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            // Convertir el usuario guardado a DTO y devolverlo
            UsuarioDTO respuesta = new UsuarioDTO();
            respuesta.setId(usuarioGuardado.getId());
            respuesta.setEmail(usuarioGuardado.getEmail());
            respuesta.setNombre(usuarioGuardado.getNombre());
            respuesta.setApellido(usuarioGuardado.getApellido());
            respuesta.setRoles(usuarioGuardado.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toSet()));

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    /**
     * Inicia sesión de un usuario en el sistema.
     */
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody Map<String, String> credenciales) {
        try {
            // Registrar la solicitud recibida para depuración
            logger.info("Solicitud de login recibida: {}", credenciales);

            String email = credenciales.get("email");
            String password = credenciales.get("password");

            logger.info("Email: {}, Password recibida (longitud): {}", email, password != null ? password.length() : "null");

            if (email == null || password == null) {
                logger.warn("Email o password faltantes en la solicitud");
                return ResponseEntity.badRequest().body(Map.of("error", "Email y password son requeridos"));
            }

            // Buscar el usuario por email
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Usuario no encontrado con email: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
            }

            Usuario usuario = usuarioOpt.get();
            logger.info("Usuario encontrado: {}", usuario.getEmail());

            // Verificar la contraseña (en un caso real, se debería verificar la contraseña encriptada)
            if (!password.equals(usuario.getPassword())) {
                logger.warn("Contraseña incorrecta para el usuario: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
            }

            // Verificar que el usuario esté activo
            if (!usuario.isActivo()) {
                logger.warn("Usuario inactivo: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario inactivo"));
            }

            // Crear respuesta
            Map<String, Object> respuesta = new HashMap<>();

            // Convertir el usuario a DTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setApellido(usuario.getApellido());
            usuarioDTO.setRoles(usuario.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toSet()));

            respuesta.put("usuario", usuarioDTO);
            respuesta.put("mensaje", "Inicio de sesión exitoso");

            logger.info("Login exitoso para el usuario: {}", email);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al iniciar sesión: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al iniciar sesión");
            errorResponse.put("mensaje", e.getMessage());
            errorResponse.put("tipo", e.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint alternativo para iniciar sesión que acepta parámetros directamente.
     * Útil para probar si hay problemas de serialización con el cuerpo JSON.
     */
    @PostMapping("/login-alt")
    public ResponseEntity<?> iniciarSesionAlternativo(
            @RequestParam String email,
            @RequestParam String password) {
        try {
            logger.info("Solicitud de login alternativo recibida - Email: {}", email);

            // Buscar el usuario por email
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Usuario no encontrado con email: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar la contraseña
            if (!password.equals(usuario.getPassword())) {
                logger.warn("Contraseña incorrecta para el usuario: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
            }

            // Verificar que el usuario esté activo
            if (!usuario.isActivo()) {
                logger.warn("Usuario inactivo: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario inactivo"));
            }

            // Crear respuesta
            Map<String, Object> respuesta = new HashMap<>();

            // Convertir el usuario a DTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setApellido(usuario.getApellido());
            usuarioDTO.setRoles(usuario.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toSet()));

            respuesta.put("usuario", usuarioDTO);
            respuesta.put("mensaje", "Inicio de sesión exitoso");

            logger.info("Login alternativo exitoso para el usuario: {}", email);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al iniciar sesión alternativo: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al iniciar sesión");
            errorResponse.put("mensaje", e.getMessage());
            errorResponse.put("tipo", e.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

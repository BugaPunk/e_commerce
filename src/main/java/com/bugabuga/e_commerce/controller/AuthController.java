package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.ApiResponse;
import com.bugabuga.e_commerce.model.dto.UsuarioDTO;
import com.bugabuga.e_commerce.security.JwtTokenUtil;
import com.bugabuga.e_commerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/jwt")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody Map<String, String> loginRequest) {
        long inicio = System.currentTimeMillis();
        logger.info("[AUTH] Inicio login: {}", inicio);

        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Generar token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            // Obtener información del usuario
            UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);

            // Crear respuesta
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("usuario", usuario);

            long fin = System.currentTimeMillis();
            logger.info("[AUTH] Fin login: {} (Duración: {} ms)", fin, (fin-inicio));

            return ResponseEntity.ok(ApiResponse.success("Inicio de sesión exitoso", data));
        } catch (BadCredentialsException e) {
            logger.warn("[AUTH] Error de credenciales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Email o contraseña incorrectos"));
        } catch (Exception e) {
            logger.error("[AUTH] Error en login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Ocurrió un error al procesar la solicitud"));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<?>> registro(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[AUTH] Inicio registro: {}", inicio);

        try {
            // Registrar usuario
            UsuarioDTO nuevoUsuario = usuarioService.registrarUsuario(usuarioDTO);

            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getPassword())
            );

            // Generar token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            // Crear respuesta
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("usuario", nuevoUsuario);

            long fin = System.currentTimeMillis();
            logger.info("[AUTH] Fin registro: {} (Duración: {} ms)", fin, (fin-inicio));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Registro exitoso", data));
        } catch (IllegalArgumentException e) {
            logger.warn("[AUTH] Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("[AUTH] Error en registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Ocurrió un error al procesar la solicitud"));
        }
    }
}

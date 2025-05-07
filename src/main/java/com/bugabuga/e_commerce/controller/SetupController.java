package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.entity.Rol;
import com.bugabuga.e_commerce.repository.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones de configuración inicial del sistema.
 * Este controlador proporciona endpoints para inicializar datos necesarios
 * para el funcionamiento del sistema, como roles, categorías, etc.
 */
@RestController
@RequestMapping("/api/setup")
public class SetupController {

    private final RolRepository rolRepository;
    private static final Logger logger = LoggerFactory.getLogger(SetupController.class);

    @Autowired
    public SetupController(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Inicializa los roles básicos del sistema si no existen.
     * Crea los roles CLIENTE, ADMIN y VENDEDOR.
     *
     * @return ResponseEntity con un mensaje de éxito y la lista de roles creados
     */
    @PostMapping("/init-roles")
    public ResponseEntity<Map<String, Object>> initRoles() {
        long inicio = System.currentTimeMillis();
        logger.info("[SETUP] Inicio initRoles: {}", inicio);
        
        List<Rol> roles = new ArrayList<>();
        
        // Crear rol CLIENTE si no existe
        if (!rolRepository.existsByNombre("CLIENTE")) {
            Rol rolCliente = new Rol();
            rolCliente.setNombre("CLIENTE");
            rolCliente.setDescripcion("Rol para usuarios clientes de la plataforma");
            roles.add(rolRepository.save(rolCliente));
            logger.info("[SETUP] Rol CLIENTE creado");
        } else {
            logger.info("[SETUP] Rol CLIENTE ya existe");
        }
        
        // Crear rol ADMIN si no existe
        if (!rolRepository.existsByNombre("ADMIN")) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setDescripcion("Rol para administradores de la plataforma");
            roles.add(rolRepository.save(rolAdmin));
            logger.info("[SETUP] Rol ADMIN creado");
        } else {
            logger.info("[SETUP] Rol ADMIN ya existe");
        }
        
        // Crear rol VENDEDOR si no existe
        if (!rolRepository.existsByNombre("VENDEDOR")) {
            Rol rolVendedor = new Rol();
            rolVendedor.setNombre("VENDEDOR");
            rolVendedor.setDescripcion("Rol para vendedores de tiendas");
            roles.add(rolRepository.save(rolVendedor));
            logger.info("[SETUP] Rol VENDEDOR creado");
        } else {
            logger.info("[SETUP] Rol VENDEDOR ya existe");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Roles inicializados correctamente");
        response.put("rolesCreados", roles.size());
        response.put("roles", roles);
        
        long fin = System.currentTimeMillis();
        logger.info("[SETUP] Fin initRoles: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Inicializa categorías básicas para productos si no existen.
     * Este método se puede implementar en el futuro para crear categorías predefinidas.
     */
    @PostMapping("/init-categorias")
    public ResponseEntity<Map<String, Object>> initCategorias() {
        // Implementación futura para inicializar categorías
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint preparado para inicializar categorías");
        return ResponseEntity.ok(response);
    }
}

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
 * Controlador simplificado para inicializar datos básicos del sistema.
 */
@RestController
@RequestMapping("/api/init")
public class InitController {

    private final RolRepository rolRepository;
    private static final Logger logger = LoggerFactory.getLogger(InitController.class);

    @Autowired
    public InitController(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Inicializa los roles básicos del sistema.
     */
    @PostMapping("/roles")
    public ResponseEntity<Map<String, Object>> initRoles() {
        List<Rol> roles = new ArrayList<>();
        
        // Crear rol CLIENTE si no existe
        if (!rolRepository.existsByNombre("CLIENTE")) {
            Rol rolCliente = new Rol();
            rolCliente.setNombre("CLIENTE");
            rolCliente.setDescripcion("Rol para usuarios clientes de la plataforma");
            roles.add(rolRepository.save(rolCliente));
            logger.info("Rol CLIENTE creado");
        } else {
            logger.info("Rol CLIENTE ya existe");
        }
        
        // Crear rol ADMIN si no existe
        if (!rolRepository.existsByNombre("ADMIN")) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setDescripcion("Rol para administradores de la plataforma");
            roles.add(rolRepository.save(rolAdmin));
            logger.info("Rol ADMIN creado");
        } else {
            logger.info("Rol ADMIN ya existe");
        }
        
        // Crear rol VENDEDOR si no existe
        if (!rolRepository.existsByNombre("VENDEDOR")) {
            Rol rolVendedor = new Rol();
            rolVendedor.setNombre("VENDEDOR");
            rolVendedor.setDescripcion("Rol para vendedores de tiendas");
            roles.add(rolRepository.save(rolVendedor));
            logger.info("Rol VENDEDOR creado");
        } else {
            logger.info("Rol VENDEDOR ya existe");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Roles inicializados correctamente");
        response.put("rolesCreados", roles.size());
        response.put("roles", roles);
        
        return ResponseEntity.ok(response);
    }
}

package com.bugabuga.e_commerce.config;

import com.bugabuga.e_commerce.model.entity.Rol;
import com.bugabuga.e_commerce.repository.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Inicializador de datos para la aplicación.
 * Se ejecuta al iniciar la aplicación y crea los datos básicos necesarios.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final RolRepository rolRepository;
    
    @Autowired
    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    
    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
    }
    
    /**
     * Inicializa los roles básicos del sistema si no existen.
     */
    private void initRoles() {
        if (rolRepository.count() == 0) {
            logger.info("Inicializando roles básicos del sistema...");
            
            List<Rol> roles = Arrays.asList(
                new Rol(null, "ADMIN", "Administrador del sistema"),
                new Rol(null, "VENDEDOR", "Vendedor de tienda"),
                new Rol(null, "CLIENTE", "Cliente regular")
            );
            
            rolRepository.saveAll(roles);
            
            logger.info("Roles básicos creados: {}", roles.size());
        } else {
            logger.info("Los roles ya existen en la base de datos. Omitiendo inicialización.");
        }
    }
}

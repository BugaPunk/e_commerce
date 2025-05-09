package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.model.dto.UsuarioDTO;
import com.bugabuga.e_commerce.model.entity.Rol;
import com.bugabuga.e_commerce.model.entity.Usuario;
import com.bugabuga.e_commerce.repository.RolRepository;
import com.bugabuga.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                         RolRepository rolRepository,
                         PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        // Verificar que el email no esté en uso
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        // Crear un nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword())); // Encriptamos la contraseña
        usuario.setActivo(true);

        // Asignar roles
        Set<Rol> roles = new HashSet<>();
        if (usuarioDTO.getRoles() != null && !usuarioDTO.getRoles().isEmpty()) {
            for (String rolNombre : usuarioDTO.getRoles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
        } else {
            // Asignar rol de cliente por defecto
            Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                    .orElseThrow(() -> new ResourceNotFoundException("Rol CLIENTE no encontrado"));
            roles.add(rolCliente);
        }
        usuario.setRoles(roles);

        // Guardar el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Convertir el usuario guardado a DTO y devolverlo
        return convertirADTO(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO autenticarUsuario(String email, String password) {
        // Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        // Verificar la contraseña usando el passwordEncoder
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        // Verificar que el usuario esté activo
        if (!usuario.isActivo()) {
            throw new IllegalArgumentException("El usuario está inactivo");
        }

        // Convertir el usuario a DTO y devolverlo
        return convertirADTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return convertirADTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        return convertirADTO(usuario);
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Verificar que el email no esté en uso por otro usuario
        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        // Actualizar los campos del usuario
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());

        // Actualizar la contraseña si se proporciona
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword())); // Encriptamos la contraseña
        }

        // Guardar el usuario actualizado
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        // Convertir el usuario actualizado a DTO y devolverlo
        return convertirADTO(usuarioActualizado);
    }

    @Transactional
    public void desactivarUsuario(Long id) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Desactivar el usuario
        usuario.setActivo(false);

        // Guardar el usuario actualizado
        usuarioRepository.save(usuario);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());

        // No incluir la contraseña en el DTO por seguridad

        // Convertir roles a conjunto de nombres de rol
        Set<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());
        dto.setRoles(roles);

        return dto;
    }
}

package com.example.crudusuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.crudusuario.model.Usuario;
import com.example.crudusuario.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor con inyección de dependencias para manejar el repositorio de usuarios
     * y la encriptación de contraseñas.
     */
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método de Spring Security para cargar un usuario por su username.
     * Se utiliza en la autenticación del sistema.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // La contraseña ya debe estar encriptada
                .authorities(usuario.getRole())  // Define los roles del usuario
                .build();
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     * La contraseña se encripta antes de guardarla por seguridad.
     * @param usuario Datos del usuario a registrar.
     * @return Usuario registrado.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Encriptar la contraseña
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return Usuario encontrado o lanza una excepción si no existe.
     */
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Lista todos los usuarios registrados en el sistema.
     * @return Lista de usuarios.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id ID del usuario a buscar.
     * @return Usuario encontrado o null si no existe.
     */
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param usuarioActualizado Datos nuevos del usuario.
     */
    public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setUsername(usuarioActualizado.getUsername()); // Actualiza el nombre de usuario
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword())); // Actualiza la contraseña encriptada
            usuario.setRole(usuarioActualizado.getRole()); // Actualiza el rol del usuario
            usuarioRepository.save(usuario);
        } else {
            throw new UsernameNotFoundException("No se encontró el usuario con ID: " + id);
        }
    }

    /**
     * Elimina un usuario del sistema por su ID.
     * @param id ID del usuario a eliminar.
     */
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}

// cl.ucm.bookapi.apibook.service.UserService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.dto.LoginRequest;
import cl.ucm.bookapi.apibook.dto.RegisterRequest;
import cl.ucm.bookapi.apibook.dto.UserResponse;
import cl.ucm.bookapi.apibook.entity.Role;
import cl.ucm.bookapi.apibook.entity.User;
import cl.ucm.bookapi.apibook.repository.RoleRepository;
import cl.ucm.bookapi.apibook.repository.UserRepository;
import cl.ucm.bookapi.apibook.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Role rol = roleRepository.findByName("LECTOR")
                .orElseThrow(() -> new RuntimeException("Error: Rol 'LECTOR' no encontrado en la base de datos."));

        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(rol);
        user.setState(true); // Asume que un nuevo usuario está activo

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getState()) {
            throw new RuntimeException("Usuario bloqueado o multado");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
    }

    /**
     * Busca un usuario por su email y devuelve un DTO con su información.
     * @param email El email del usuario a buscar.
     * @return Un objeto UserResponse con los detalles del usuario.
     * @throws EntityNotFoundException Si el usuario con el email especificado no se encuentra.
     */
    public UserResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado."));

        // Mapear la entidad User al DTO UserResponse
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getState(),
                user.getRole() != null ? user.getRole().getName() : null
        );
    }

    /**
     * Actualiza el estado (activo/inactivo) de un usuario.
     * @param email El email del usuario cuyo estado se desea actualizar.
     * @param newState El nuevo estado a asignar (true para activo, false para inactivo/bloqueado).
     * @return Un UserResponse con los detalles del usuario actualizado.
     * @throws EntityNotFoundException Si el usuario con el email especificado no se encuentra.
     */
    public UserResponse updateUserState(String email, Boolean newState) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado para actualizar su estado."));

        user.setState(newState); // Actualiza el estado
        User updatedUser = userRepository.save(user); // Guarda los cambios

        // Retorna el DTO del usuario actualizado
        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getState(),
                updatedUser.getRole() != null ? updatedUser.getRole().getName() : null
        );
    }
}
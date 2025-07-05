// cl.ucm.bookapi.apibook.service.UserService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.dto.LoginRequest;
import cl.ucm.bookapi.apibook.dto.RegisterRequest; // Mantener si RegisterDto es solo un alias o si aún se usa en otro lado
import cl.ucm.bookapi.apibook.dto.UserResponse;
import cl.ucm.bookapi.apibook.entity.Role;
import cl.ucm.bookapi.apibook.entity.User;
import cl.ucm.bookapi.apibook.repository.RoleRepository;
import cl.ucm.bookapi.apibook.repository.UserRepository;
import cl.ucm.bookapi.apibook.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- ¡Nueva importación!

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

    // Asumiré que RegisterDto es lo mismo que RegisterRequest por ahora.
    // Si RegisterDto es una clase nueva, deberías crearla como hicimos con UpdateUserStateRequest.
    // Si es solo un renombramiento, puedes usar RegisterRequest en el parámetro del método.
    // Para este ejemplo, lo dejaremos como RegisterRequest para que compile directamente con lo que ya tienes.
    // Si quieres que se llame RegisterDto, dime y creamos esa clase.
    @Transactional // <-- ¡Añadida!
    public Optional<UserResponse> createUser(RegisterRequest request) { // <-- Cambiado el retorno a Optional<UserResponse>
        if (userRepository.existsByEmail(request.getEmail())) {
            // Ya no lanzamos excepción, devolvemos Optional.empty()
            return Optional.empty(); // Indicamos que el registro falló por email duplicado
        }

        Optional<Role> roleOptional = roleRepository.findByName("LECTOR");
        if (roleOptional.isEmpty()) {
            // Ya no lanzamos excepción, devolvemos Optional.empty()
            return Optional.empty(); // Indicamos que el registro falló por rol no encontrado
        }
        Role rol = roleOptional.get();

        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(rol);
        user.setState(true); // Asume que un nuevo usuario está activo

        User savedUser = userRepository.save(user);

        // Mapear el usuario guardado a UserResponse y devolverlo en un Optional
        return Optional.of(new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getState(),
                savedUser.getRole() != null ? savedUser.getRole().getName() : null
        ));
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

    public UserResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado."));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getState(),
                user.getRole() != null ? user.getRole().getName() : null
        );
    }

    public UserResponse updateUserState(String email, Boolean newState) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado para actualizar su estado."));

        user.setState(newState);
        User updatedUser = userRepository.save(user);

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
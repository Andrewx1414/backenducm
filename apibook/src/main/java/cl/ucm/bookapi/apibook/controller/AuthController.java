// cl.ucm.bookapi.apibook.controller.AuthController.java
package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.RegisterRequest;
import cl.ucm.bookapi.apibook.dto.LoginRequest;
import cl.ucm.bookapi.apibook.dto.UserResponse; // <-- ¡Nueva importación!
import cl.ucm.bookapi.apibook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- ¡Nueva importación!
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional; // Ya debería estar importado

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) { // <-- Cambiado el retorno a ResponseEntity<?>
        Optional<UserResponse> registeredUser = userService.createUser(request); // <-- Llamada al nuevo método

        if (registeredUser.isPresent()) {
            // Si el registro fue exitoso, devuelve 201 Created con los datos del usuario
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.get());
        } else {
            // Si el Optional está vacío, significa que el email ya está registrado o el rol no se encontró
            // Aquí, devolvemos un 409 Conflict o 400 Bad Request con un mensaje descriptivo.
            // El 409 Conflict es más específico para recursos duplicados (email ya existe).
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error al registrar usuario: el email ya está registrado o el rol de lector no existe.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }
}
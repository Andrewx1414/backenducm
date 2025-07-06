package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.RegisterRequest;
import cl.ucm.bookapi.apibook.dto.LoginRequest;
import cl.ucm.bookapi.apibook.dto.UserResponse;
import cl.ucm.bookapi.apibook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional; 

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Optional<UserResponse> registeredUser = userService.createUser(request);

        if (registeredUser.isPresent()) {
           
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.get());
        } else {

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
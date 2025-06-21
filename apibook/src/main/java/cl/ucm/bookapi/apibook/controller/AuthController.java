package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.RegisterRequest;
import cl.ucm.bookapi.apibook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.ucm.bookapi.apibook.dto.LoginRequest;

@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    String token = userService.login(request);
    return ResponseEntity.ok(token);
}


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}

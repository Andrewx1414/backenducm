package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.UserResponse;
import cl.ucm.bookapi.apibook.dto.UpdateUserStateRequest;
import cl.ucm.bookapi.apibook.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reader")
public class UserController { 
    @Autowired
    private UserService userService;

    // Endpoint para buscar la información de un usuario (lector) por su email.
    @GetMapping("/find/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTOR')")
    public ResponseEntity<?> findReaderByEmail(@PathVariable("email") String email) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName();

        // Un LECTOR solo puede buscar su propio email. Un ADMIN puede buscar cualquier email.
        boolean isLector = authentication.getAuthorities().stream()

                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_LECTOR")); 

        if (isLector) {
            if (!authenticatedUserEmail.equalsIgnoreCase(email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("🚫 Acceso denegado: Un Lector solo puede consultar sus propios datos.");
            }
        }

        try {
            UserResponse userResponse = userService.findUserByEmail(email);
            return ResponseEntity.ok(userResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el lector: " + e.getMessage());
        }
    }

    // Endpoint para cambiar el estado (activo/inactivo) de un usuario.
    @PostMapping("/state/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateReaderState(
            @PathVariable("email") String email,
            @RequestBody UpdateUserStateRequest request) {

        if (request.getNewState() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El campo 'newState' es obligatorio.");
        }

        try {
            UserResponse updatedUser = userService.updateUserState(email, request.getNewState());
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado del lector: " + e.getMessage());
        }
    }
}
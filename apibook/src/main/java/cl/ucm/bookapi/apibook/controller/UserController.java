// cl.ucm.bookapi.apibook.controller.UserController.java
package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.UserResponse;
import cl.ucm.bookapi.apibook.dto.UpdateUserStateRequest; // ¡Nueva importación!
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

    /**
     * Endpoint para buscar la información de un usuario (lector) por su email.
     * Accesible por ADMIN (para buscar cualquier email) y LECTOR (para buscar su propio email).
     *
     * GET: {url_base}/reader/find/{email}
     *
     * @param email El email del usuario a buscar.
     * @return Un objeto UserResponse con los detalles del usuario, o 404 NOT FOUND si no se encuentra.
     */
    @GetMapping("/find/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTOR')") // Accesible para ADMIN y LECTOR
    public ResponseEntity<?> findReaderByEmail(@PathVariable String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName(); // El email del usuario autenticado (principal)

        // Lógica de seguridad adicional para el rol LECTOR:
        // Un LECTOR solo puede buscar su propio email. Un ADMIN puede buscar cualquier email.
        boolean isLector = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("LECTOR") || a.getAuthority().equalsIgnoreCase("ROLE_LECTOR"));

        if (isLector) {
            // Si el usuario es un LECTOR, debe coincidir el email de la URL con el email de su token
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

    /**
     * Endpoint para cambiar el estado (activo/inactivo) de un usuario.
     * Solo accesible por usuarios con rol ADMIN.
     *
     * POST: {url_base}/reader/state/{email}
     * Request Body: { "newState": true/false }
     *
     * @param email El email del usuario cuyo estado se desea actualizar.
     * @param request El DTO que contiene el nuevo estado (true para activo, false para inactivo/bloqueado).
     * @return Un objeto UserResponse con los detalles del usuario actualizado, o un mensaje de error.
     */
    @PostMapping("/state/{email}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los ADMIN pueden cambiar el estado de los usuarios
    public ResponseEntity<?> updateReaderState(
            @PathVariable String email,
            @RequestBody UpdateUserStateRequest request) {

        if (request.getNewState() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El campo 'newState' es obligatorio.");
        }

        try {
            UserResponse updatedUser = userService.updateUserState(email, request.getNewState());
            return ResponseEntity.ok(updatedUser); // Retorna 200 OK con el DTO del usuario actualizado
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retorna 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado del lector: " + e.getMessage()); // Retorna 500 Internal Server Error
        }
    }
}
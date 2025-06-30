// cl.ucm.bookapi.apibook.controller.FineController.java
package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.entity.Fine;
import cl.ucm.bookapi.apibook.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Asegúrate de tener Spring Security configurado
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fine") // Endpoint base para multas
public class FineController {

    @Autowired
    private FineService fineService;

    // --- ¡ENDPOINT PARA BUSCAR MULTAS POR EMAIL! ---
    // Este endpoint debería ser accesible por ADMINs o por el propio usuario (ROLE_USER)
    // que busca sus multas. Lo protegemos para ADMIN por ahora.
    @GetMapping("/searchByEmail")
    @PreAuthorize("hasRole('ADMIN','LECTOR')") // O "hasAnyRole('ADMIN', 'USER')" si el usuario puede ver sus propias multas
    public ResponseEntity<List<Fine>> getFinesByEmail(@RequestParam String email) {
        List<Fine> fines = fineService.getFinesByUserEmail(email);
        if (fines.isEmpty()) {
            // Podríamos devolver 404 si no se encuentran multas para ese email
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fines);
        }
        return new ResponseEntity<>(fines, HttpStatus.OK);
    }
}
package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.entity.Fine;
import cl.ucm.bookapi.apibook.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fine")
public class FineController {

    @Autowired
    private FineService fineService;

    // ENDPOINT ADMIN/LECTOR PARA BUSCAR MULTAS POR EMAIL
    @GetMapping("/searchByEmail")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTOR')")
    public ResponseEntity<List<Fine>> getFinesByEmail(@RequestParam("email") String email) {
        List<Fine> fines = fineService.getFinesByUserEmail(email);
        if (fines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(fines, HttpStatus.OK);
    }
}
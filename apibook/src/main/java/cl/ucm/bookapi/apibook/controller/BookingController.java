package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.BookingRequest;
import cl.ucm.bookapi.apibook.dto.BookingResponse; // Importar el DTO de respuesta
import cl.ucm.bookapi.apibook.entity.Booking; // Necesario para el retorno de createBooking
import cl.ucm.bookapi.apibook.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Endpoint para crear una nueva reserva de libro.
     * Protegido para el rol ADMIN.
     * POST: {url_base}/booking/new
     * Request Body: { "userId": 1, "copyBookId": 10 }
     */
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            Booking newBooking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("📚 Reserva creada correctamente con ID: " + newBooking.getId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la reserva: " + e.getMessage());
        }
    }

    /**
     * Endpoint para buscar reservas (préstamos) por el email del usuario.
     * Accesible por ADMIN (para buscar cualquier email) y LECTOR (para buscar su propio email).
     * GET: {url_base}/booking/find/{email}
     * @param email El email del usuario cuyas reservas se desean buscar.
     * @return Una lista de BookingResponse DTOs, o 404 NOT FOUND si no se encuentran.
     */
    @GetMapping("/find/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTOR')")
    public ResponseEntity<List<BookingResponse>> getBookingsByEmail(@PathVariable String email) {
        List<BookingResponse> bookings = bookingService.getBookingsByUserEmail(email);

        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * Endpoint para procesar la devolución de un libro.
     * Protegido para el rol ADMIN.
     * POST: {url_base}/booking/return/{idBooking}
     * @param idBooking El ID de la reserva a devolver.
     * @return Una confirmación de la devolución o un mensaje de error.
     */
    @PostMapping("/return/{idBooking}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los ADMINs pueden procesar devoluciones
    public ResponseEntity<?> returnBook(@PathVariable Long idBooking) {
        try {
            Booking returnedBooking = bookingService.returnBook(idBooking);
            // Podrías devolver el DTO de la reserva actualizada o un mensaje simple
            return ResponseEntity.ok("✅ Libro de la reserva ID " + returnedBooking.getId() + " devuelto correctamente. Copia del libro ID " + returnedBooking.getCopyBook().getIdCopyBook() + " disponible nuevamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            // Esto capturará "La reserva ya ha sido devuelta" o inconsistencias.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al procesar la devolución: " + e.getMessage());
        }
    }
}
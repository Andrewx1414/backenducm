package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.BookingRequest;
import cl.ucm.bookapi.apibook.dto.BookingResponse;
import cl.ucm.bookapi.apibook.entity.Booking;
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

    //Endpoint para crear una nueva reserva de libro.
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

    //Endpoint para buscar reservas (préstamos) por el email del usuario.
    @GetMapping("/find/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTOR')")
    public ResponseEntity<List<BookingResponse>> getBookingsByEmail(@PathVariable("email") String email) { 
        List<BookingResponse> bookings = bookingService.getBookingsByUserEmail(email);

        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    //Endpoint para la devolución de un libro.
    @PostMapping("/return/{idBooking}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> returnBook(@PathVariable("idBooking") Long idBooking) {
        try {
            Booking returnedBooking = bookingService.returnBook(idBooking);
            return ResponseEntity.ok("Libro de la reserva ID " + returnedBooking.getId() + " devuelto correctamente. Copia del libro ID " + returnedBooking.getCopyBook().getIdCopyBook() + " disponible nuevamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al procesar la devolución: " + e.getMessage());
        }
    }
}
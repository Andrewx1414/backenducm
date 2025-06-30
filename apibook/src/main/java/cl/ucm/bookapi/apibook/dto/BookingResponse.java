// cl.ucm.bookapi.apibook.dto.BookingResponse.java
package cl.ucm.bookapi.apibook.dto;

import java.time.LocalDateTime;

public class BookingResponse {
    private Long id; // ID de la reserva
    private LocalDateTime dateBooking; // Fecha y hora de la reserva
    private LocalDateTime dateReturn; // Fecha y hora de la devolución (puede ser null)
    private Boolean state; // Estado de la reserva (activo/devuelto)
    private Long userId; // ID del usuario que realizó la reserva
    private BookingCopyBookDTO copyBook; // Detalles de la copia del libro y el libro asociado

    // Constructor vacío (necesario para la deserialización)
    public BookingResponse() {}

    // Constructor con todos los campos (útil para el mapeo desde la entidad)
    public BookingResponse(Long id, LocalDateTime dateBooking, LocalDateTime dateReturn, Boolean state, Long userId, BookingCopyBookDTO copyBook) {
        this.id = id;
        this.dateBooking = dateBooking;
        this.dateReturn = dateReturn;
        this.state = state;
        this.userId = userId;
        this.copyBook = copyBook;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(LocalDateTime dateBooking) {
        this.dateBooking = dateBooking;
    }

    public LocalDateTime getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(LocalDateTime dateReturn) {
        this.dateReturn = dateReturn;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BookingCopyBookDTO getCopyBook() {
        return copyBook;
    }

    public void setCopyBook(BookingCopyBookDTO copyBook) {
        this.copyBook = copyBook;
    }
}
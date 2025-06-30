// cl.ucm.bookapi.apibook.dto.BookingRequest.java
package cl.ucm.bookapi.apibook.dto;

// No se necesitan imports adicionales para tipos básicos

public class BookingRequest {

    private Long userId;     // ID del usuario que realiza la reserva
    private Long copyBookId; // ID de la copia del libro que se desea reservar

    // Constructor vacío (necesario para la deserialización JSON)
    public BookingRequest() {}

    // Constructor con todos los campos (opcional, pero útil)
    public BookingRequest(Long userId, Long copyBookId) {
        this.userId = userId;
        this.copyBookId = copyBookId;
    }

    // --- Getters y Setters ---

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCopyBookId() {
        return copyBookId;
    }

    public void setCopyBookId(Long copyBookId) {
        this.copyBookId = copyBookId;
    }
}
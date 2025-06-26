// cl.ucm.bookapi.apibook.dto.NewCopyBookRequest.java
package cl.ucm.bookapi.apibook.dto;

public class NewCopyBookRequest {
    private Long bookId; // Solo necesitamos el ID del libro

    // Constructor vacío
    public NewCopyBookRequest() {}

    // Constructor
    public NewCopyBookRequest(Long bookId) { // Es común usar el mismo nombre del campo para el constructor
        this.bookId = bookId;
    }

    // Getter y Setter
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
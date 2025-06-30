// cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO.java
package cl.ucm.bookapi.apibook.dto;

public class BookingCopyBookDTO {
    private Long idCopyBook; // ID de la copia del libro
    private String bookTitle; // Título del libro asociado a la copia
    private String bookAuthor; // Autor del libro asociado a la copia
    private String bookType; // Tipo del libro asociado a la copia

    // Constructor vacío
    public BookingCopyBookDTO() {}

    // Constructor con todos los campos
    public BookingCopyBookDTO(Long idCopyBook, String bookTitle, String bookAuthor, String bookType) {
        this.idCopyBook = idCopyBook;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookType = bookType;
    }

    // --- Getters y Setters ---

    public Long getIdCopyBook() {
        return idCopyBook;
    }

    public void setIdCopyBook(Long idCopyBook) {
        this.idCopyBook = idCopyBook;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
}
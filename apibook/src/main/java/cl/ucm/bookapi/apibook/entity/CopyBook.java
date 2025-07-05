// cl.ucm.bookapi.apibook.entity.CopyBook.java
package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "library_copy_book") // Asegúrate que el nombre de la tabla coincida exactamente
public class CopyBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_copybook") // Nombre de la columna PK
    private Long idCopyBook;

    // Relación ManyToOne con Book
    // Esto crea la columna de clave foránea #book_fk en library_copy_book
    @ManyToOne
    @JoinColumn(name = "book_fk", nullable = false) // Nombre de la columna FK en tu BD
    private Book book;

    @Column(name = "unique_code", unique = true, nullable = false) // <-- ¡Campo nuevo añadido!
    private String uniqueCode; // El código único de esta copia del libro

    @Column(name = "state") // Columna para el estado (ej: 1 para disponible, 0 para prestado)
    private Boolean state; // O String, si prefieres "DISPONIBLE", "PRESTADO", etc. Integer es común para 0/1.

    // Constructor vacío
    public CopyBook() {}

    // Constructor con libro (opcional, útil para inicializar)
    // Se recomienda añadir uniqueCode aquí también
    public CopyBook(Book book, String uniqueCode) { // <-- Constructor actualizado
        this.book = book;
        this.uniqueCode = uniqueCode; // <-- Asignar el nuevo campo
        this.state = true; // Por defecto, una nueva copia está disponible
    }

    // --- Getters y Setters ---

    public Long getIdCopyBook() {
        return idCopyBook;
    }

    public void setIdCopyBook(Long idCopyBook) {
        this.idCopyBook = idCopyBook;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUniqueCode() { // <-- Getter nuevo
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) { // <-- Setter nuevo
        this.uniqueCode = uniqueCode;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
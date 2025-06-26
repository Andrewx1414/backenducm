// cl.ucm.bookapi.apibook.entity.CopyBook.java (o LibraryCopyBook.java)
package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "library_copy_book") // Asegúrate que el nombre de la tabla coincida exactamente
public class CopyBook { // O LibraryCopyBook

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_copybook") // Nombre de la columna PK
    private Long idCopyBook;

    // Relación ManyToOne con Book
    // Esto crea la columna de clave foránea #book_fk en library_copy_book
    @ManyToOne
    @JoinColumn(name = "book_fk", nullable = false) // Nombre de la columna FK en tu BD
    private Book book;

    @Column(name = "state") // Columna para el estado (ej: 1 para disponible, 0 para prestado)
    private Boolean state; // O String, si prefieres "DISPONIBLE", "PRESTADO", etc. Integer es común para 0/1.

    // Constructor vacío
    public CopyBook() {}

    // Constructor con libro (opcional, útil para inicializar)
    public CopyBook(Book book) {
        this.book = book;
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
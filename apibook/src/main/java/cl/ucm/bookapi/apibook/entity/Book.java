package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "library_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private Long idBook;

    private String author;
    private String title;
    private String type;

    // --- ¡ESTE ES EL CAMBIO CLAVE QUE TE FALTA HACER EN BOOK.JAVA! ---
    @Lob // Sigue siendo útil para mapear a TEXT y decirle a Hibernate que puede ser un CLOB grande
    @Column(name = "image64")
    private String image64; // <-- ¡ASEGÚRATE DE QUE ESTO SEA 'String', NO 'String[]'!

    // Getters y setters (estos ya deberían estar correctos si los cambiaste a String)

    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Asegúrate de que estos getters y setters manejen un String simple
    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }
}
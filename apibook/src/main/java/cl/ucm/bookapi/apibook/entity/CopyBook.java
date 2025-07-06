package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "library_copy_book")
public class CopyBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_copybook")
    private Long idCopyBook;

    @ManyToOne
    @JoinColumn(name = "book_fk", nullable = false)
    private Book book;

    @Column(name = "unique_code", unique = true, nullable = false)
    private String uniqueCode;

    @Column(name = "state")
    private Boolean state;

    public CopyBook() {}

    public CopyBook(Book book, String uniqueCode) {
        this.book = book;
        this.uniqueCode = uniqueCode;
        this.state = true;
    }

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

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
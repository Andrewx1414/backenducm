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

    @Column(name = "state")
    private Boolean state;

    public CopyBook() {}

    public CopyBook(Book book) {
        this.book = book;
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
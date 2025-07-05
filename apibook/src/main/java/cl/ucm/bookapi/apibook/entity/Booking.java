// cl.ucm.bookapi.apibook.entity.Booking.java
package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "library_booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copybook_fk", nullable = false)
    private CopyBook copyBook;

    @Column(name = "date_booking", nullable = false)
    private LocalDateTime dateBooking;

    @Column(name = "date_return")
    private LocalDateTime dateReturn;

    @Column(name = "expected_return_date", nullable = false) // <-- ¡Campo nuevo añadido!
    private LocalDateTime expectedReturnDate;

    @Column(name = "state", nullable = false)
    private Boolean state;

    public Booking() {}

    // Constructor actualizado para incluir expectedReturnDate
    public Booking(User user, CopyBook copyBook, LocalDateTime dateBooking, LocalDateTime expectedReturnDate, Boolean state) {
        this.user = user;
        this.copyBook = copyBook;
        this.dateBooking = dateBooking;
        this.expectedReturnDate = expectedReturnDate; // Asignar el nuevo campo
        this.state = state;
    }

    // --- Getters y Setters para todos los campos ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CopyBook getCopyBook() {
        return copyBook;
    }

    public void setCopyBook(CopyBook copyBook) {
        this.copyBook = copyBook;
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

    public LocalDateTime getExpectedReturnDate() { // <-- Getter nuevo
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { // <-- Setter nuevo
        this.expectedReturnDate = expectedReturnDate;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
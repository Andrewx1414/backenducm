// cl.ucm.bookapi.apibook.entity.Booking.java
package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime; // Importar para manejar fechas y horas

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "library_booking") // Confirma que este es el nombre correcto de tu tabla de reservas
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Nombre de la columna PK en library_booking
    private Long id;

    // Relación ManyToOne con User (un usuario puede tener muchas reservas)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading es bueno para relaciones ManyToOne por defecto
    @JoinColumn(name = "user_fk", nullable = false) // Nombre de la columna FK en tu BD
    
    private User user; // El usuario que realizó la reserva

    // Relación ManyToOne con CopyBook (una copia puede ser reservada muchas veces)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
    @JoinColumn(name = "copybook_fk", nullable = false) // Nombre de la columna FK en tu BD
    
    private CopyBook copyBook; // La copia de libro que se reservó

    @Column(name = "date_booking", nullable = false) // Fecha y hora en que se realizó la reserva
    private LocalDateTime dateBooking;

    @Column(name = "date_return") // Fecha y hora de devolución (puede ser nula inicialmente)
    private LocalDateTime dateReturn;

    @Column(name = "state", nullable = false) // Estado de la reserva (ej. true = activa, false = devuelta/cancelada)
    private Boolean state; // Mapea 'bool' de la DB a Boolean

    // Constructor vacío requerido por JPA
    public Booking() {}

    // Constructor útil para crear nuevas reservas
    public Booking(User user, CopyBook copyBook, LocalDateTime dateBooking, Boolean state) {
        this.user = user;
        this.copyBook = copyBook;
        this.dateBooking = dateBooking;
        this.state = state;
        // dateReturn se establecerá en la devolución
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
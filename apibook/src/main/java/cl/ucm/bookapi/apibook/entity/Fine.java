// cl.ucm.bookapi.apibook.entity.Fine.java
package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "library_fine") // Confirma que este es el nombre correcto de tu tabla de multas
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // ¡Importante! Mapea a la columna 'id' que es tu PK en la DB 
    private Long id; // Cambiar nombre del campo a 'id' para claridad si prefieres, o mantener idFine

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false) // Columna user_fk es NOT NULL 
    private User user; // Apunta a library_user.id 

    @ManyToOne
    @JoinColumn(name = "booking_fk") // Columna booking_fk puede ser NULL 
    private Booking booking; // ¡Necesitas una entidad Booking! Apunta a library_booking.id 

    @Column(name = "amount", nullable = false) // Tipo numeric(10,2) y NOT NULL 
    private BigDecimal amount;

    @Column(name = "description") // Tipo text y puede ser NULL 
    private String description;

    @Column(name = "fine_date", nullable = false) // Tipo timestamp y NOT NULL 
    private LocalDateTime fineDate;

    @Column(name = "state", nullable = false) // Tipo varchar(50) y NOT NULL 
    private String state; // Importante: Usar String porque la DB lo tiene como varchar

    // Constructor vacío
    public Fine() {
        this.fineDate = LocalDateTime.now(); // Por defecto, la fecha actual
        this.state = "PENDIENTE"; // Valor por defecto si es String, ej: "PENDIENTE" o "PAGADA"
    }

    // --- Getters y Setters ---

    public Long getId() { // Cambiar a getId() si el campo es 'id'
        return id;
    }

    public void setId(Long id) { // Cambiar a setId() si el campo es 'id'
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getFineDate() {
        return fineDate;
    }

    public void setFineDate(LocalDateTime fineDate) {
        this.fineDate = fineDate;
    }

    public String getState() { // Getter para String
        return state;
    }

    public void setState(String state) { // Setter para String
        this.state = state;
    }
}
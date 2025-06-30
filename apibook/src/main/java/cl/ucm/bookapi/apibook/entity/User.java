package cl.ucm.bookapi.apibook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "library_user") // Correcto, coincide con tu DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Puedes añadir @Column si el nombre de la columna es diferente, sino es opcional
    private Long id;

    @Column(name = "name") // Añadido @Column
    private String name;

    @Column(name = "last_name") // Asumo que el campo en DB es 'last_name', si es 'lastName' puedes omitir name="last_name"
    private String lastName;

    @Column(name = "email", unique = true, nullable = false) // ¡Importante! email debe ser único y no nulo
    private String email;

    @Column(name = "password", nullable = false) // ¡Importante! password no puede ser nulo
    private String password;

    @Column(name = "state") // Si en DB es 'state'
    private Boolean state;

    @ManyToOne(fetch = FetchType.EAGER) // ¡Sugerencia! Cargar el rol inmediatamente
    @JoinColumn(name = "role_id", nullable = false) // ¡Importante! role_id no puede ser nulo, asumido de tu descripción
    private Role role;

    // ¡Sugerencia! Añadir constructor vacío explícito para JPA
    public User() {}

    // Puedes añadir un constructor para facilitar la creación de usuarios, si lo deseas
    // public User(String name, String lastName, String email, String password, Boolean state, Role role) {
    //     this.name = name;
    //     this.lastName = lastName;
    //     this.email = email;
    //     this.password = password;
    //     this.state = state;
    //     this.role = role;
    // }


    // Getters y setters (estos están bien)
    public Long getId() {
        return id;
    }

    public void setId(Long id) { // Setter para el ID, aunque generalmente JPA lo maneja
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
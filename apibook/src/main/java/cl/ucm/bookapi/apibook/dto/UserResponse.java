package cl.ucm.bookapi.apibook.dto;

public class UserResponse {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Boolean state;
    private String roleName;


    public UserResponse() {}

    public UserResponse(Long id, String name, String lastName, String email, Boolean state, String roleName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.state = state;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
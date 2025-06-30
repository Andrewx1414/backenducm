// cl.ucm.bookapi.apibook.dto.UpdateUserStateRequest.java
package cl.ucm.bookapi.apibook.dto;

public class UpdateUserStateRequest {
    private Boolean newState; // El nuevo estado que se desea asignar al usuario

    // Constructor vacío
    public UpdateUserStateRequest() {}

    // Constructor con el campo
    public UpdateUserStateRequest(Boolean newState) {
        this.newState = newState;
    }

    // Getter y Setter
    public Boolean getNewState() {
        return newState;
    }

    public void setNewState(Boolean newState) {
        this.newState = newState;
    }
}
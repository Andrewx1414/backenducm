package cl.ucm.bookapi.apibook.dto;

public class UpdateUserStateRequest {
    private Boolean newState;

    public UpdateUserStateRequest() {}

    public UpdateUserStateRequest(Boolean newState) {
        this.newState = newState;
    }

    public Boolean getNewState() {
        return newState;
    }

    public void setNewState(Boolean newState) {
        this.newState = newState;
    }
}
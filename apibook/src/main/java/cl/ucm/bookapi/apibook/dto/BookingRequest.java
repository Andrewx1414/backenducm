package cl.ucm.bookapi.apibook.dto;


public class BookingRequest {

    private Long userId;
    private Long copyBookId;

  
    public BookingRequest() {}

    public BookingRequest(Long userId, Long copyBookId) {
        this.userId = userId;
        this.copyBookId = copyBookId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCopyBookId() {
        return copyBookId;
    }

    public void setCopyBookId(Long copyBookId) {
        this.copyBookId = copyBookId;
    }
}
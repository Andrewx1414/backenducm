package cl.ucm.bookapi.apibook.dto;

public class NewCopyBookRequest {
    private Long bookId;

    public NewCopyBookRequest() {}

    public NewCopyBookRequest(Long bookId) { 
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
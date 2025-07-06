package cl.ucm.bookapi.apibook.dto;

import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private LocalDateTime dateBooking;
    private LocalDateTime dateReturn;
    private Boolean state;
    private Long userId;
    private BookingCopyBookDTO copyBook;

    public BookingResponse() {}

    public BookingResponse(Long id, LocalDateTime dateBooking, LocalDateTime dateReturn, Boolean state, Long userId, BookingCopyBookDTO copyBook) {
        this.id = id;
        this.dateBooking = dateBooking;
        this.dateReturn = dateReturn;
        this.state = state;
        this.userId = userId;
        this.copyBook = copyBook;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BookingCopyBookDTO getCopyBook() {
        return copyBook;
    }

    public void setCopyBook(BookingCopyBookDTO copyBook) {
        this.copyBook = copyBook;
    }
}
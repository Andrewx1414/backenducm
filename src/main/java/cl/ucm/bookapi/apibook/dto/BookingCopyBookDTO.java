package cl.ucm.bookapi.apibook.dto;

public class BookingCopyBookDTO {
    private Long idCopyBook;
    private String bookTitle;
    private String bookAuthor;
    private String bookType;


    public BookingCopyBookDTO() {}

    public BookingCopyBookDTO(Long idCopyBook, String bookTitle, String bookAuthor, String bookType) {
        this.idCopyBook = idCopyBook;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookType = bookType;
    }


    public Long getIdCopyBook() {
        return idCopyBook;
    }

    public void setIdCopyBook(Long idCopyBook) {
        this.idCopyBook = idCopyBook;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
}
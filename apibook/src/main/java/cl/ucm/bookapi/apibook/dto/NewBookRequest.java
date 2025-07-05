package cl.ucm.bookapi.apibook.dto;

public class NewBookRequest {
    private String title;
    private String author;
    private String type;
    private String image64;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImage64() { return image64; }
    public void setImage64(String image64) { this.image64 = image64; }
}
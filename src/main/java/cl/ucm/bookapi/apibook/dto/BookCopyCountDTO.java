package cl.ucm.bookapi.apibook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCopyCountDTO {
    private String bookTitle;
    private Long copyCount;
}
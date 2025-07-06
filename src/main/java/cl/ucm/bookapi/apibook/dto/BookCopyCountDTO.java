package cl.ucm.bookapi.apibook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, equals y hashCode
@AllArgsConstructor // Genera un constructor con todos los argumentos
@NoArgsConstructor // Genera un constructor sin argumentos (necesario para JPA si usas otros constructores)
public class BookCopyCountDTO {
    private String bookTitle;
    private Long copyCount; // Usamos Long para el conteo de copias
}
package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Asegúrate de que esta importación esté

import java.util.List;

@Repository // Asegúrate de que esta anotación esté
public interface BookRepository extends JpaRepository<Book, Long> { // <-- ¡CAMBIA INTEGER POR LONG AQUÍ!

    // Método para obtener todos los libros (ya lo tienes)
    List<Book> findAll(); // Si lo necesitas y no está implícito por JpaRepository

    // Método para buscar libros por tipo
    List<Book> findByType(String type);

    // Método para buscar libros por título
    List<Book> findByTitleContainingIgnoreCase(String title);
}
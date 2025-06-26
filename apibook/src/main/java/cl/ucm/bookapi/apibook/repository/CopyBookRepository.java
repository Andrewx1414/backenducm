// cl.ucm.bookapi.apibook.repository.CopyBookRepository.java
package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.CopyBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopyBookRepository extends JpaRepository<CopyBook, Long> {
    // Puedes añadir métodos de búsqueda personalizados aquí en el futuro si los necesitas
}